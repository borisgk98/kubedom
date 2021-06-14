import orjson
import requests as requests
import websockets
import os.path
import paramiko as ssh
import time
from kubedom.utils import test_external_ip
from bash import bash

import kubedom.config as CONFIG
import kubedom.vboxapi.api as api

__MTYPE = 'type'
__MDATA = 'data'

__OVA_FILE_LOCATION = "/tmp/customer-node.ova"
__CONFIG_PATH_LOCAL = "/tmp/config.json"
__CONFIG_PATH_REMOTE = "/home/vm-provider/config.json"
__VM_PROVIDER_LOGIN = "vm-provider"
__VM_PROVIDER_PASSWORD = "vm-provider"
__VM_HOST = 'localhost'
__VM_PORT = 7722
__MAX_RETRIES = 10

async def consumer_handler(websocket: websockets.WebSocketClientProtocol) -> None:
    async for message in websocket:
        print(f"Consume message\n:{message}")
        wrapper = __parse_json(message)
        mtype = wrapper[__MTYPE]
        try:
            if mtype == 'CUSTOMER_NODE_CREATION':
                print('Customer node creation start')
                dto = __parse_json(wrapper[__MDATA])
                ova_location = dto['ovaLocation']
                machine_name = dto['machineName']
                __download_and_save(ova_location)
                api.create_machine(__OVA_FILE_LOCATION, machine_name)
                api.start(machine_name)
                if not __copy_config(dto['customerNodeConfig']):
                    api.remove(machine_name)
                print('Customer node creation finish successfully')
            elif mtype == 'CUSTOMER_NODE_REMOVING':
                print('Customer node removing start')
                dto = __parse_json(wrapper[__MDATA])
                machine_name = dto['machineName']
                api.remove(machine_name)
                print('Customer node removing finish successfully')
        except Exception as e:
            print("Some error while processing message")
            print(e)


async def consumer(hostname: str, port: int, path: str):
    web_socket_resource_url = f"ws://{hostname}:{port}{path}"
    external_ip = __get_external_ip()
    headers = [
        ("Provider-node-token", CONFIG.TOKEN),
        ("Provider-node-device-uuid", CONFIG.NODE_UUID),
        ("Provider-node-external-ip", external_ip),
        ("Provider-node-is-primary", __is_primary(external_ip))
    ]
    async with websockets.connect(web_socket_resource_url, extra_headers=headers) as websocket:
        await consumer_handler(websocket)


def __download_and_save(ova_location: str):
    if os.path.isfile(__OVA_FILE_LOCATION):
        print(f"Use file from cache ({__OVA_FILE_LOCATION})")
        return
    print("Download ova image")
    r = requests.get(ova_location, allow_redirects=True)
    open(__OVA_FILE_LOCATION, 'wb').write(r.content)
    print("Download finished")


def __copy_config(config: str):
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


def __parse_json(message: str):
    return orjson.loads(message)


def __get_external_ip():
    return bash('dig +short myip.opendns.com @resolver1.opendns.com').value()


def __is_primary(external_id: str):
    return test_external_ip(external_id, 6443)
