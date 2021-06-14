import requests
import time
import subprocess

__MAX_RETRIES = 10


def test_external_ip(ip: str, port: int):
    try:
        server_process = subprocess.Popen(['python3', 'test-server.py', '--port', str(port)],
                               stdout=subprocess.PIPE,
                               universal_newlines=True)
        retries = 0
        while retries <= __MAX_RETRIES:
            try:
                resp = requests.get(f"http://{ip}:{port}")
                server_process.kill()
                return resp.content == b'test' and resp.status_code == 200
            except Exception:
                retries += 1
                time.sleep(2)

        if retries > __MAX_RETRIES:
            server_process.kill()
            return False
        else:
            server_process.kill()
            return True
    except Exception:
        return False


if __name__ == "__main__":
    print(test_external_ip("localhost", 6443))


