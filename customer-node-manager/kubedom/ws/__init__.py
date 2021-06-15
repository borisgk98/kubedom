import orjson
import websockets
import logging
import json
from bash import bash
import os

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
                node_name = accept_dto['nodeName']
                logging.info("Create k3s master node")
                creation_info = __master_creation(external_ip, node_name)
                logging.info("Successfully created k3s master node")
                dto = {
                    'type': 'K3S_MASTER_CREATION_RESPONSE',
                    'data': json.dumps(creation_info)
                }
                send_data = json.dumps(dto)
                logging.info("Send token and kubectl config to server")
                logging.debug(f"Sending data: {send_data}")
                await websocket.send(send_data)
            elif mtype == 'K3S_WORKER_CREATION':
                accept_dto = __parse_json(wrapper[__MDATA])
                external_ip = accept_dto['masterExternalIp']
                token = accept_dto['masterToken']
                node_name = accept_dto['nodeName']
                logging.info("Create k3s worker node")
                __worker_creation(external_ip, token, node_name)
                logging.info("Successfully created k3s worker node")
                dto = {
                    'type': 'K3S_WORKER_CREATION_RESPONSE'
                }
                send_data = json.dumps(dto)
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


def __master_creation(external_ip: str, node_name: str):
    bash(f'curl -sfL https://get.k3s.io | '
         f'sh -s - server --tls-san "{external_ip}" '
         f'--kube-apiserver-arg advertise-address={external_ip} '
         f'--kube-apiserver-arg external-hostname={external_ip} '
         f'--node-name {node_name}')
    token = open('/var/lib/rancher/k3s/server/node-token').read()
    kubectl_config = open('/etc/rancher/k3s/k3s.yaml').read()
    return {
        'token': token,
        'kubectlConfig': kubectl_config
    }
#     TODO check status


def __worker_creation(master_ip: str, master_token: str, node_name: str):
    command = f'curl -sfL https://get.k3s.io | sh -s - agent --token {master_token} --server https://{master_ip}:6443 --node-name {node_name}'
    __execute_command(command)
#     TODO check status

def __execute_command(command: str):
    logging.info(f'Execute: {command}')
    os.system(command)
