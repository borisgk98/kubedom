import asyncio
from kubedom.ws import consumer
from kubedom.const import *

# TODO wss
if __name__ == "__main__":
    print("Start")
    loop = asyncio.get_event_loop()
    loop.run_until_complete(consumer(SERVER_URL, 8081, WS_PATH))
    loop.run_forever()