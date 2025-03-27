


--------------------------------------------------------------------------
### v1 - Pods creation + basic-networking ( using ClusterIP, NodePort)
--------------------------------------------------------------------------

```bash
kubectl apply -f voting-app-v1.yaml
kubectl get pods
kubectl get svc

kubectl port-forward --address 0.0.0.0 svc/vote 30001:80
kubectl port-forward --address 0.0.0.0 svc/result 30002:80

kubectl delete -f voting-app-v1.yaml
```

--------------------------------------------------------------------------
### v2 - scheduling pods on specific nodes
--------------------------------------------------------------------------

```bash
kubectl label node kind-cluster-worker role=db
kubectl apply -f voting-app-v2.yaml
kubectl get pods -o wide
kubectl get svc

kubectl delete -f voting-app-v2.yaml
```

--------------------------------------------------------------------------
### v3 - Pods with persistent storage
--------------------------------------------------------------------------

```bash

# ✅ 1️⃣ Install NFS Server on your host (Ubuntu/Debian):

sudo apt update
sudo apt install -y nfs-kernel-server

# ✅ 2️⃣ Create and configure the NFS export directory:

sudo mkdir -p /nfs/data/postgres
sudo chown -R nobody:nogroup /nfs/data/postgres
sudo chmod -R 777 /nfs/data/postgres

# ✅ 3️⃣ Export the directory in /etc/exports:

sudo nano /etc/exports
# add below line
/nfs/data/postgres *(rw,sync,no_subtree_check,no_root_squash)

sudo exportfs -rav
sudo systemctl restart nfs-kernel-server

sudo exportfs -v

# ✅ 4️⃣ Install NFS client on your k8s nodes where db pods will be scheduled:

docker ps
docker exec -it 66d5ca72f5c3 /bin/bash
apt update
apt install -y nfs-common
apt update && apt install -y nfs-common
showmount -e 10.0.0.4


# ✅ 5️⃣ Create a PersistentVolume and PersistentVolumeClaim:

kubectl apply -f voting-app-v3.yaml

kubectl get pv
kubectl get pvc

kubectl get pods -o wide
kubectl get svc

kubectl delete -f voting-app-v3.yaml
```

--------------------------------------------------------------------------
### v4 - Pods with ConfigMap and Secrets
--------------------------------------------------------------------------


```bash
kubectl apply -f voting-app-v4.yaml
kubectl get pods
kubectl delete -f voting-app-v4.yaml
```


--------------------------------------------------------------------------
### v6 -ConfigMap and Secret
--------------------------------------------------------------------------


```bash
kubectl apply -f voting-app-v6.yaml
kubectl get pods
kubectl delete -f voting-app-v6.yaml
```

--------------------------------------------------------------------------
### v7 - Pod with Liveness and Readiness Probe ( health checks)
--------------------------------------------------------------------------
```bash
kubectl apply -f voting-app-v5.yaml
kubectl get pods
kubectl delete -f voting-app-v5.yaml
```

--------------------------------------------------------------------------



--------------------------------------------------------------------------
### Loadbalancer Service ( cloud provider specific)
--------------------------------------------------------------------------

### create k8s cluster on Azure
```bash
az group create --name nag-rg --location centralindia
az aks create \
    --resource-group nag-rg \
    --name nag-aks \
    --generate-ssh-keys \
    --node-count 3 \
    --zones 1 2 3
kubectl get nodes -o wide
kubectl get nodes --show-labels
```

```bash
kubectl apply -f voting-app-v6.yaml
kubectl get pods -w
kubectl delete -f voting-app-v6.yaml

```



--------------------------------------------------------------------------
### ingress controller
--------------------------------------------------------------------------


--------------------------------------------------------------------------
### Ingress Service  ( ingress-nginx) / ( ingrress-traefik)
--------------------------------------------------------------------------

on kind k8s cluster,

```bash
kubectl apply -f https://kind.sigs.k8s.io/examples/ingress/deploy-ingress-nginx.yaml
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=90s

kubectl apply -f voting-app-v7.yaml
kubectl get pods
kubectl get svc -n ingress-nginx 
curl -H "Host: vote.local" http://172.18.0.5:<nodeport>
curl -H "Host: result.local" http://172.18.0.5:<nodeport>

sudo nano /etc/hosts
# add below line
172.1.0.5 vote.local result.local

http://vote.local:<nodeport>
http://result.local:<nodeport>

#  ( for Mac users)
kubectl port-forward --address 0.0.0.0 -n ingress-nginx svc/ingress-nginx-controller 8080:80


kubectl delete -f voting-app-v7.yaml

```

