import logging
import traceback

import requests
import time
import subprocess
import os
import orjson
import os.path
import paramiko as ssh
from bash import bash

from kubedom.const import OVA_FILE_LOCATION

__CONFIG_PATH_LOCAL = "/tmp/config.json"
__CONFIG_PATH_REMOTE = "/home/vm-provider/config.json"
__VM_PROVIDER_LOGIN = "vm-provider"
__VM_PROVIDER_PASSWORD = "vm-provider"
__VM_HOST = 'localhost'
__VM_PORT = 7722
__MAX_RETRIES = 10


def test_external_ip(ip: str, port: int):
    try:
        logging.info("Check port is available")
        test_server_path = os.path.dirname(os.path.abspath(__file__)) + '/../test-server.py'
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


def parse_json(message: str):
    return orjson.loads(message)


def get_external_ip():
    return bash('dig +short myip.opendns.com @resolver1.opendns.com').value()


def is_primary(external_id: str):
    return test_external_ip(external_id, 6443)


def download_and_save(ova_location: str):
    if os.path.isfile(OVA_FILE_LOCATION):
        logging.info(f"Use file from cache ({OVA_FILE_LOCATION})")
        return
    logging.info("Download ova image")
    bash(f"wget --no-cache -O {OVA_FILE_LOCATION} {ova_location}")
    logging.info("Download finished")


def copy_config(config: str):
    logging.info("Copy config to customer vm via ssh")

    open(__CONFIG_PATH_LOCAL, 'w').write(config)
    retry = 0
    host = __VM_HOST
    port = __VM_PORT
    remotepath = __CONFIG_PATH_REMOTE
    localpath = __CONFIG_PATH_LOCAL
    while retry <= __MAX_RETRIES:
        try:
            transport = ssh.Transport((host, port))
            transport.connect(username=__VM_PROVIDER_LOGIN, password=__VM_PROVIDER_PASSWORD)
            sftp = ssh.SFTPClient.from_transport(transport)
            sftp.put(localpath, remotepath)
            sftp.close()
            transport.close()
            break
        except Exception:
            retry += 1
            time.sleep(5)
    return retry <= __MAX_RETRIES



if __name__ == "__main__":
    logging.info(test_external_ip("localhost", 6443))


