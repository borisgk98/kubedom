export PYTHONPATH=$PYTHONPATH:/usr/lib/virtualbox/:/opt/vboxsdk/sdk/bindings/xpcom/python/
/usr/bin/python3 /opt/kubedom/provider-node-manager/main.py 1> /var/log/kubedom/app.log 2> /var/log/kubedom/error.log &
PID=$!
echo $PID > /run/provider-node-manager.pid