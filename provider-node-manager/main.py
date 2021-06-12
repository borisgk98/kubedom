import asyncio
from kubedom.ws import consumer

if __name__ == "__main__":
    loop = asyncio.get_event_loop()
    loop.run_until_complete(consumer("localhost", 80, "/api/kubedom/ws/provider"))
    loop.run_forever()