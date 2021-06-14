import requests
import time
import subprocess

process = subprocess.Popen(['python3', 'test-server.py', '--port', "6443"],
                           stdout=subprocess.PIPE,
                           universal_newlines=True)

__MAX_RETRIES = 10
retries = 0
while retries <= __MAX_RETRIES:
    try:
        resp = requests.get("http://localhost:6443", timeout=10)
        print(resp.status_code)
        print(resp.content == b'test')
        break
    except Exception:
        retries += 1
        time.sleep(2)

if retries > __MAX_RETRIES:
    print(False)
else:
    print(True)

