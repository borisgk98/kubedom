# should run under root
# required: wget, unzip, virtualbox, python3, pip
echo "Installing wget, unzip, virtualbox, python3, pip"
export DEBIAN_FRONTEND=noninteractive
apk-get -y install wget unzip virtualbox python3 pip

# vboxapi
echo "Installing virtualbox api"
wget https://download.virtualbox.org/virtualbox/6.1.22/VirtualBoxSDK-6.1.22-144080.zip -P /tmp -O vboxsdk.zip
mkdir /opt/vboxsdk
cd /opt/vboxsdk
unzip /tmp/vboxsdk.zip
cd sdk/installer
python3 vboxapisetup.py install

# customer node
echo "Download provider-node-manager"
mkdir /opt/kubedom/
cd /opt/kubedom/
wget https://www.kubedom.borisgk.space/static/provider-node-manager.zip
echo "Installing provider-node-manager"
unzip provider-node-manager.zip
cd provider-node-manager
mkdir /var/log/kubedom
# TODO install to python virtualenv
echo "Installing required python packages"
python3 -m pip install -r requirements.txt

echo "Installing required python packages"
export PYTHONPATH=$PYTHONPATH:/usr/lib/virtualbox/:/opt/vboxsdk/sdk/bindings/xpcom/python/
python3 main.py 1> /var/log/kubedom/app.log 2> /var/log/kubedom/error.log
