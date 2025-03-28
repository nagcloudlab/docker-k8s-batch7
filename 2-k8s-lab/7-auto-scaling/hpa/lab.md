




### HPA

```bash

kubectl apply -f nginx.yaml

kubectl get deployment metrics-server -n kube-system
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
kubectl patch deployment metrics-server -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--kubelet-insecure-tls"}]'
kubectl get pods -n kube-system
kubectl top nodes
kubectl top pods
kubectl get hpa

kubectl delete -f nginx.yaml
kubectl get pods
kubectl apply -f nginx-hpa.yaml
kubectl get hpa

kubectl run -i --tty load-generator --rm --image=busybox -- /bin/sh -c "while true; do wget -q -O- http://nginx; done"

kubectl delete -f nginx-hpa.yaml
kubectl delete -f nginx.yaml

```
