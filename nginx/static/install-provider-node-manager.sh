# should run under root
# required: wget, unzip, virtualbox, python3, pip
cd /tmp
echo "Installing wget, unzip, virtualbox, python3, pip"
export DEBIAN_FRONTEND=noninteractive
apt-get -y install wget unzip virtualbox python3 pip
# shellcheck disable=SC2028
echo '\n'

# vboxapi
echo "Installing virtualbox api"
wget https://download.virtualbox.org/virtualbox/6.1.22/VirtualBoxSDK-6.1.22-144080.zip -P /tmp -O vboxsdk.zip
mkdir /opt/vboxsdk
cd /opt/vboxsdk
unzip /tmp/vboxsdk.zip
cd sdk/installer
python3 vboxapisetup.py install
echo '\n'

# customer node
echo "Download provider-node-manager"
mkdir /opt/kubedom/
cd /opt/kubedom/
wget https://www.kubedom.borisgk.space/static/provider-node-manager.zip
echo '\n'

echo "Installing provider-node-manager"
mkdir provider-node-manager
cd provider-node-manager
unzip ../provider-node-manager.zip
cp kubedom-provider-node-manager.service /etc/systemd/system/kubedom-provider-node-manager.service
mkdir /etc/kubedom
cp config.ini /etc/kubedom/config.ini
mkdir /var/log/kubedom
echo '\n'

# TODO install to python virtualenv
echo "Installing required python packages"
python3 -m pip install -r requirements.txt
echo '\n'

echo "Finish. Lets configure '/etc/kubedom/config.ini' and start service 'systemctl start kubedom-provider-node-manager.service'"
#export PYTHONPATH=$PYTHONPATH:/usr/lib/virtualbox/:/opt/vboxsdk/sdk/bindings/xpcom/python/
#python3 main.py 1> /var/log/kubedom/app.log 2> /var/log/kubedom/error.log
