import orjson
import websockets
import logging
import json
from bash import bash

__MTYPE = 'type'
__MDATA = 'data'
__NODE_ID = 'customerNodeId'

async def consumer_handler(websocket: websockets.WebSocketClientProtocol) -> None:
    async for message in websocket:
        logging.info(f"Consume message\n:{message}")
        wrapper = __parse_json(message)
        mtype = wrapper[__MTYPE]
        try:
            if mtype == 'K3S_MASTER_CREATION':
                accept_dto = __parse_json(wrapper[__MDATA])
                external_ip = accept_dto['externalIp']
                logging.info("Create k3s master node")
                creation_info = __master_creation(external_ip)
                logging.info("Successfully created k3s master node")
                dto = {
                    'type': 'K3S_MASTER_CREATION_RESPONSE',
                    'data': json.dumps(creation_info)
                }
                send_data = json.dumps(dto)
                logging.info("Send token and kubectl config to server")
                logging.debug(f"Sending data: {send_data}")
                await websocket.send(send_data)
        except Exception as e:
            logging.critical(e, exc_info=True)

async def consumer(hostname: str, port: int, path: str, config_str: str):
    web_socket_resource_url = f"ws://{hostname}:{port}{path}"
    config = __parse_json(config_str)
    headers = [
        ("Customer-node-device-uuid", config[__NODE_ID])
    ]
    async with websockets.connect(web_socket_resource_url, extra_headers=headers) as websocket:
        await consumer_handler(websocket)


def __parse_json(message: str):
    return orjson.loads(message)


def __master_creation(external_ip: str):
    bash(f'curl -sfL https://get.k3s.io | sh -s - server --tls-san "{external_ip}"')
    token = open('/var/lib/rancher/k3s/server/node-token').read()
    kubectl_config = open('/etc/rancher/k3s/k3s.yaml').read()
    return {
        'token': token,
        'kubectlConfig': kubectl_config
    }
