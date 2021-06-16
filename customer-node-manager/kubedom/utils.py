import logging
import os

import orjson

from kubedom.const import START_WORKER_SCRIPT_PATH


def parse_json(message: str):
    return orjson.loads(message)


def master_creation(external_ip: str, node_name: str):
    command = f'curl -sfL https://get.k3s.io | sh -s - server --tls-san "{external_ip}" --kube-apiserver-arg advertise-address={external_ip} --kube-apiserver-arg external-hostname={external_ip} --node-name {node_name}'.replace('\n', '')
    execute_command(command)
    token = open('/var/lib/rancher/k3s/server/node-token').read()
    kubectl_config = open('/etc/rancher/k3s/k3s.yaml').read()
    return {
        'token': token,
        'kubectlConfig': kubectl_config
    }
#     TODO check status


def worker_creation(master_ip: str, master_token: str, node_name: str):
    master_url = f'https://{master_ip}:6443'
    command = f'{START_WORKER_SCRIPT_PATH} {master_url} {master_token} {node_name}'.replace('\n', '')
    execute_command(command)
#     TODO check status


def execute_command(command: str):
    logging.info(f'Execute: {command}')
    os.system(command)
