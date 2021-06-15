export PYTHONPATH=$PYTHONPATH:/usr/lib/virtualbox/:/opt/vboxsdk/sdk/bindings/xpcom/python/
/usr/bin/python3 /opt/kubedom/provider-node-manager/main.py &
PID=$!
echo $PID > /run/provider-node-manager.pid