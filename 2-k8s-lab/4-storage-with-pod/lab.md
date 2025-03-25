


```bash
kubectl delete -f java-web-service-pod.yaml
kubectl apply -f java-web-service-pod.yaml
kubectl get pods
kubectl exec -it java-web-service-pod-1 -c java-web-service-container -- /bin/sh
apk update
apk add curl
curl localhost:8080/hello
kubectl exec -it java-web-service-pod-1 -c log-reader-container -- /bin/sh
```
