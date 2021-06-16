import asyncio
import json
import socket

import websockets

from kubedom.const import *
from kubedom.utils import *


class WSClient:

    def __init__(self, **kwargs):
        self.url = f"ws://{SERVER_URL}:{SERVER_PORT}{WS_PATH}"
        # set some default values
        self.reply_timeout = kwargs.get('reply_timeout') or 10
        self.ping_timeout = kwargs.get('ping_timeout') or 5
        self.sleep_time = kwargs.get('sleep_time') or 5
        config_json = open(LOCAL_CONFIG_PATH).read()
        config = parse_json(config_json)
        __NODE_ID = 'customerNodeId'
        self.node_id = config[__NODE_ID]

    async def listen_forever(self):
        while True:
            # outer loop restarted every time the connection fails
            logging.info('Creating new connection...')
            headers = [
                ("Customer-node-device-uuid", self.node_id)
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
                        await self.callback(message, ws)
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

    async def callback(self, message, ws):
        __MTYPE = 'type'
        __MDATA = 'data'
        logging.info(f"Consume message\n:{message}")
        wrapper = parse_json(message)
        mtype = wrapper[__MTYPE]
        try:
            if mtype == 'K3S_MASTER_CREATION':
                accept_dto = parse_json(wrapper[__MDATA])
                external_ip = accept_dto['externalIp']
                node_name = accept_dto['nodeName']
                logging.info("Create k3s master node")
                creation_info = master_creation(external_ip, node_name)
                logging.info("Successfully created k3s master node")
                dto = {
                    'type': 'K3S_MASTER_CREATION_RESPONSE',
                    'data': json.dumps(creation_info)
                }
                send_data = json.dumps(dto)
                logging.info("Send token and kubectl config to server")
                logging.debug(f"Sending data: {send_data}")
                await ws.send(send_data)
            elif mtype == 'K3S_WORKER_CREATION':
                accept_dto = parse_json(wrapper[__MDATA])
                external_ip = accept_dto['masterExternalIp']
                token = accept_dto['masterToken']
                node_name = accept_dto['nodeName']
                logging.info("Create k3s worker node")
                worker_creation(external_ip, token, node_name)
                logging.info("Successfully created k3s worker node")
                dto = {
                    'type': 'K3S_WORKER_CREATION_RESPONSE'
                }
                send_data = json.dumps(dto)
                logging.debug(f"Sending data: {send_data}")
                await ws.send(send_data)
        except Exception as e:
            logging.critical(e, exc_info=True)


def start_ws_client(client):
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(client.listen_forever())


def start():
    ws_client = WSClient()
    start_ws_client(ws_client)