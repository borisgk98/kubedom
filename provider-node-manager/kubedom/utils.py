import logging
import traceback

import requests
import time
import subprocess
import os

__MAX_RETRIES = 10


def test_external_ip(ip: str, port: int):
    try:
        logging.info("Check port is available")
        test_server_path =os.path.dirname(os.path.abspath(__file__)) + '/../test-server.py'
        logging.info(f"test_server_path = {test_server_path}")
        server_process = subprocess.Popen(['python3', test_server_path, '--port', str(port)],
                               stdout=subprocess.PIPE,
                               universal_newlines=True)
        retries = 0
        while retries <= __MAX_RETRIES:
            try:
                resp = requests.get(f"http://{ip}:{port}")
                logging.info(resp)
                server_process.kill()
                return resp.content == b'test' and resp.status_code == 200
            except Exception:
                logging.info(f"Retry {retries}")
                retries += 1
                time.sleep(2)

        if retries > __MAX_RETRIES:
            server_process.kill()
            return False
        else:
            server_process.kill()
            return True
    except Exception:
        logging.error("Some error while testing ip")
        logging.error(traceback.format_exc())
        return False


if __name__ == "__main__":
    logging.info(test_external_ip("localhost", 6443))


