import asyncio
import os
import time
import shutil
import logging
from kubedom.ws import consumer
from kubedom.const import *

__PROVIDER_CONFIG_PATH = "/home/vm-provider/config.json"
__LOCAL_CONFIG_PATH = "/etc/kubedom/config.json"
__LOG_PATH = '/var/log/kubedom/customer.log'

# TODO wss
if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, filename=__LOG_PATH)

    logging.info("Start customer node manager")
    # Waiting while create file /home/vm-provider/config.json
    while True:
        if os.path.isfile(__PROVIDER_CONFIG_PATH):
            shutil.copy(__PROVIDER_CONFIG_PATH, __LOCAL_CONFIG_PATH)
            break
        else:
            logging.info("Waiting provider-node-manager create /home/vm-provider/config.json")
            time.sleep(5)

    config = open(__LOCAL_CONFIG_PATH).read()

    loop = asyncio.get_event_loop()
    loop.run_until_complete(consumer(SERVER_URL, 8081, WS_PATH, config))
    loop.run_forever()