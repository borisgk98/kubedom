import asyncio
import os
import time
import shutil
from kubedom.ws import consumer
from kubedom.const import *

__PROVIDER_CONFIG_PATH = "/home/vm-provider/config.json"
__LOCAL_CONFIG_PATH = "/etc/kubedom/config.json"
# __LOCAL_CONFIG_PATH = "/home/boris/.kubedom/config.json"

# TODO wss
if __name__ == "__main__":
    # Waiting while create file /home/vm-provider/config.json
    while True:
        if os.path.isfile(__PROVIDER_CONFIG_PATH):
            shutil.copy(__PROVIDER_CONFIG_PATH, __LOCAL_CONFIG_PATH)
            break
        else:
            print("Waiting provider-node-manager create /home/vm-provider/config.json")
            time.sleep(5)

    config = open(__LOCAL_CONFIG_PATH).read()

    loop = asyncio.get_event_loop()
    loop.run_until_complete(consumer(SERVER_URL, 8081, WS_PATH, config))
    loop.run_forever()