import orjson
import requests as requests
import websockets
import os.path

import kubedom.config as CONFIG
import kubedom.vboxapi.api as api

__MTYPE = 'type'
__MDATA = 'data'

__OVA_FILE_LOCATION = "/tmp/customer-node.ova"

async def consumer_handler(websocket: websockets.WebSocketClientProtocol) -> None:
    async for message in websocket:
        print(f"Consume message\n:{message}")
        wrapper = __parse_json(message)
        mtype = wrapper[__MTYPE]
        if mtype == 'CUSTOMER_NODE_CREATION':
            dto = __parse_json(wrapper[__MDATA])
            ova_location = dto['ovaLocation']
            machine_name = dto['machineName']
            __download_and_save(ova_location)
            api.create_machine(__OVA_FILE_LOCATION, machine_name)


async def consumer(hostname: str, port: int, path: str):
    web_socket_resource_url = f"ws://{hostname}:{port}{path}"
    headers = [
        ("Provider-node-token", CONFIG.TOKEN),
        ("Provider-node-device-uuid", CONFIG.NODE_UUID)
    ]
    async with websockets.connect(web_socket_resource_url, extra_headers=headers) as websocket:
        await consumer_handler(websocket)


def __download_and_save(ova_location: str):
    if os.path.isfile(__OVA_FILE_LOCATION):
        return
    r = requests.get(ova_location, allow_redirects=True)
    open(__OVA_FILE_LOCATION, 'wb').write(r.content)


def __parse_json(message: str):
    return orjson.loads(message)