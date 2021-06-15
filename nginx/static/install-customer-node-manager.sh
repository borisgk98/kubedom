# should run under root
# required: wget, unzip, python3, pip
echo "Create user vm-provider"
useradd -m vm-provider

cd /tmp
echo "Installing wget, unzip, python3, pip"
export DEBIAN_FRONTEND=noninteractive
apt-get -y install wget unzip python3 pip
# shellcheck disable=SC2028
echo '\n'

# customer node
echo "Download customer-node-manager"
mkdir /opt/kubedom/
cd /opt/kubedom/
wget https://www.kubedom.borisgk.space/static/customer-node-manager.zip
echo '\n'

echo "Installing customer-node-manager"
mkdir customer-node-manager
cd customer-node-manager
unzip ../customer-node-manager.zip
cp kubedom-customer-node-manager.service /etc/systemd/system/kubedom-customer-node-manager.service
mkdir /etc/kubedom
mkdir /var/log/kubedom
echo '\n'

# TODO install to python virtualenv
echo "Installing required python packages"
python3 -m pip install -r requirements.txt
echo '\n'

echo "Start kubedom-customer-node-manager.service"
systemctl start kubedom-customer-node-manager.service
systemctl enable kubedom-customer-node-manager.service
echo "Finish"
