import asyncio
import logging
from kubedom.ws import consumer
from kubedom.const import *

__LOG_PATH = '/var/log/kubedom/provider.log'

# TODO wss
if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, filename=__LOG_PATH)

    logging.info("Start")
    loop = asyncio.get_event_loop()
    loop.run_until_complete(consumer(SERVER_URL, 8081, WS_PATH))
    loop.run_forever()