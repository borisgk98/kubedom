export K3S_URL=$1
export K3S_TOKEN=$2
export K3S_NODE_NAME=$3
curl -sfL https://get.k3s.io | sh -s - agent