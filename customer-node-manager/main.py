import logging
import os
import shutil
import time

from kubedom.const import *
from kubedom.ws import start

# TODO wss
if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, filename=LOG_PATH)

    logging.info("Start customer node manager")
    # Waiting while create file /home/vm-provider/config.json
    while True:
        if os.path.isfile(PROVIDER_CONFIG_PATH):
            shutil.copy(PROVIDER_CONFIG_PATH, LOCAL_CONFIG_PATH)
            break
        else:
            logging.info("Waiting provider-node-manager create /home/vm-provider/config.json")
            time.sleep(5)

    start()
