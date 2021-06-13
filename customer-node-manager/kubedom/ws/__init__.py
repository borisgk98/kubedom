import orjson
import websockets
import logging

__MTYPE = 'type'
__MDATA = 'data'
__NODE_ID = 'customerNodeId'

async def consumer_handler(websocket: websockets.WebSocketClientProtocol) -> None:
    async for message in websocket:
        logging.info(f"Consume message\n:{message}")
        wrapper = __parse_json(message)
        mtype = wrapper[__MTYPE]
        # if mtype == 'CUSTOMER_NODE_CREATION':


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
