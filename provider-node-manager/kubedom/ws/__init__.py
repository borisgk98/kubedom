import asyncio

import websockets
import socket
from kubedom.const import *
from kubedom.utils import *

import kubedom.config as CONFIG
import kubedom.vboxapi.api as api


class WSClient:

    def __init__(self, **kwargs):
        self.url = f"ws://{SERVER_URL}:{SERVER_PORT}{WS_PATH}"
        # set some default values
        self.reply_timeout = kwargs.get('reply_timeout') or 10
        self.ping_timeout = kwargs.get('ping_timeout') or 5
        self.sleep_time = kwargs.get('sleep_time') or 5
        self.external_ip = get_external_ip()
        self.is_primary = is_primary(self.external_ip)

    async def listen_forever(self):
        while True:
            # outer loop restarted every time the connection fails
            logging.debug('Creating new connection...')
            headers = [
                ("Provider-node-token", CONFIG.TOKEN),
                ("Provider-node-device-uuid", CONFIG.NODE_UUID),
                ("Provider-node-external-ip", self.external_ip),
                ("Provider-node-is-primary", self.is_primary)
            ]
            try:
                async with websockets.connect(self.url, extra_headers=headers) as ws:
                    while True:
                        # listener loop
                        try:
                            message = await asyncio.wait_for(ws.recv(), timeout=self.reply_timeout)
                        except (asyncio.TimeoutError, websockets.exceptions.ConnectionClosed):
                            try:
                                pong = await ws.ping()
                                await asyncio.wait_for(pong, timeout=self.ping_timeout)
                                logging.debug('Ping OK, keeping connection alive...')
                                continue
                            except:
                                logging.debug(
                                    'Ping error - retrying connection in {} sec (Ctrl-C to quit)'.format(self.sleep_time))
                                await asyncio.sleep(self.sleep_time)
                                break
                        logging.debug('Server said > {}'.format(message))
                        self.callback(message)
            except socket.gaierror:
                logging.debug(
                    'Socket error - retrying connection in {} sec (Ctrl-C to quit)'.format(self.sleep_time))
                await asyncio.sleep(self.sleep_time)
                continue
            except ConnectionRefusedError:
                logging.debug('Nobody seems to listen to this endpoint. Please check the URL.')
                logging.debug('Retrying connection in {} sec (Ctrl-C to quit)'.format(self.sleep_time))
                await asyncio.sleep(self.sleep_time)
                continue

    def callback(self, data):
        __MTYPE = 'type'
        __MDATA = 'data'
        wrapper = parse_json(data)
        mtype = wrapper[__MTYPE]
        try:
            if mtype == 'CUSTOMER_NODE_CREATION':
                logging.info('Customer node creation start')
                dto = parse_json(wrapper[__MDATA])
                ova_location = dto['ovaLocation']
                machine_name = dto['machineName']
                download_and_save(ova_location)
                api.create_machine(OVA_FILE_LOCATION, machine_name)
                api.start(machine_name)
                if not copy_config(dto['customerNodeConfig']):
                    api.remove(machine_name)
                else:
                    logging.info('Customer node creation finish successfully')
            elif mtype == 'CUSTOMER_NODE_REMOVING':
                logging.info('Customer node removing start')
                dto = parse_json(wrapper[__MDATA])
                machine_name = dto['machineName']
                api.remove(machine_name)
                logging.info('Customer node removing finish successfully')
        except Exception:
            logging.error("Some error while processing message")
            logging.error(traceback.format_exc())


def start_ws_client(client):
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(client.listen_forever())


def start():
    ws_client = WSClient()
    start_ws_client(ws_client)
