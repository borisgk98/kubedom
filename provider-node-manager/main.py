import logging

from kubedom.ws import start

__LOG_PATH = '/var/log/kubedom/provider.log'

# TODO wss
if __name__ == "__main__":
    # logging.basicConfig(level=logging.INFO, filename=__LOG_PATH)
    logging.basicConfig(level=logging.INFO)

    logging.info("Start")
    start()
