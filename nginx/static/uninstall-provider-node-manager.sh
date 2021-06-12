# should run under root
systemctl stop kubedom-provider-node-manager.service
systemctl disable kubedom-provider-node-manager.service
rm /etc/systemd/system/kubedom-provider-node-manager.service
rm -rf /opt/vboxsdk
rm -rf /opt/kubedom
rm -rf /etc/kubedom
rm -rf /var/log/kubedom
