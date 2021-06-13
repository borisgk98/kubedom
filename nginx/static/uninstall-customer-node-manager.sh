# should run under root
systemctl stop kubedom-customer-node-manager.service
systemctl disable kubedom-customer-node-manager.service
rm /etc/systemd/system/kubedom-customer-node-manager.service
rm -rf /opt/kubedom
rm -rf /etc/kubedom
rm -rf /var/log/kubedom
