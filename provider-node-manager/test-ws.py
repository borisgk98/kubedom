
# WS client example

import asyncio
import sys

import websockets
import uuid
import sys
import configparser


CONFIG_PATH = '/home/boris/.kubedom/node.ini'
SECTION = 'configuration'
config = configparser.ConfigParser()
config.read(CONFIG_PATH)
if not config.has_section(SECTION) or not config.has_option(SECTION, 'Token'):
    print("Incorrect config")
    sys.exit(0)
TOKEN = config[SECTION]['Token']
NODE_UUID = None
if not config.has_option(SECTION, 'Node_uuid'):
    NODE_UUID = uuid.uuid4()
    config.set(SECTION, 'Node_uuid', str(NODE_UUID))
    with open(CONFIG_PATH, 'w') as configfile:
        config.write(configfile)
else:
    NODE_UUID = config.get(SECTION, 'Node_uuid')
print(TOKEN, NODE_UUID)


async def consumer_handler(websocket: websockets.WebSocketClientProtocol) -> None:
    async for message in websocket:
        print(f"Consume {message}")
        


async def consumer(hostname: str, port: int, path: str):
    web_socket_resource_url = f"ws://{hostname}:{port}{path}"
    headers = [
        ("Provider-node-token", TOKEN),
        ("Provider-node-device-uuid", NODE_UUID)
    ]
    async with websockets.connect(web_socket_resource_url, extra_headers=headers) as websocket:
        await consumer_handler(websocket)


loop = asyncio.get_event_loop()
loop.run_until_complete(consumer("localhost", 80, "/api/kubedom/ws/provider"))
loop.run_forever()