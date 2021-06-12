# should run under root
# required: wget, unzip, virtualbox, python3, pip

# vboxapi
wget https://download.virtualbox.org/virtualbox/6.1.22/VirtualBoxSDK-6.1.22-144080.zip -P /tmp -O vboxsdk.zip
mkdir /opt/vboxsdk
cd /opt/vboxsdk
unzip /tmp/vboxsdk.zip
cd sdk/installer
python3 vboxapisetup.py install

# customer node
# TODO download customer-node-manager, download packages and create systemd service
pip3 install virtualbox

export PYTHONPATH=$PYTHONPATH:/usr/lib/virtualbox/:/opt/vboxsdk/sdk/bindings/xpcom/python/
python3 main.py
