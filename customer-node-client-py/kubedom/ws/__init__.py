import orjson
import websockets

import kubedom.config as CONFIG
import kubedom.vboxapi.api as api

__MTYPE = 'type'
__MDATA = 'data'

__OVA_LOCATION = "/home/boris/Documents/Ubuntu_k8s_node_1.ova"
__MACHINE_NAME = 'test'

async def consumer_handler(websocket: websockets.WebSocketClientProtocol) -> None:
    async for message in websocket:
        print(f"Consume message\n:{message}")
        dto = __parse_message(message)
        mtype = dto[__MTYPE]
        if mtype == 'CUSTOMER_NODE_CREATION':
            api.create_machine(__OVA_LOCATION, __MACHINE_NAME)


async def consumer(hostname: str, port: int, path: str):
    web_socket_resource_url = f"ws://{hostname}:{port}{path}"
    headers = [
        ("Provider-node-token", CONFIG.TOKEN),
        ("Provider-node-device-uuid", CONFIG.NODE_UUID)
    ]
    async with websockets.connect(web_socket_resource_url, extra_headers=headers) as websocket:
        await consumer_handler(websocket)


def __parse_message(message: str):
    return orjson.loads(message)