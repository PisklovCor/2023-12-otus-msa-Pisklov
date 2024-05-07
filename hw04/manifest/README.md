## Манифесты описывают сущности: Deployment, Service, Ingress, Secret

Приложение отвечает на хосту http://arch.homework

### Проверка curl
````
curl http://arch.homework/health
curl http://arch.homework/rewrite/health
````

### Проверка swagger
````
http://arch.homework/swagger-ui/index.html
````

### Применить манифесты одной командой kubectl
````
cd .\hw04\manifest\
kubectl apply -f .
````

### Команды взаимодействия (minikube, kubectl)
````
minikube dashboard

minikube addons enable ingress

minikube tunnel

pods/replicaset/deploy/svc/ingress/secret/configmap
kubectl get 
kubectl describe 
````

### Создание/удаление kind: Deployment
````
kubectl apply -f deployment.yaml
kubectl delete -f deployment.yaml
````

### Создание/удаление kind: Service
````
kubectl apply -f service.yaml
kubectl delete -f service.yaml
````

### Создание/удаление kind: Ingress
````
kubectl apply -f ingress.yaml
kubectl delete -f ingress.yaml
````

### Создание/удаление kind: Secret
````
kubectl apply -f secret.yaml
kubectl delete -f secret.yaml
````

### Создание/удаление kind: ConfigMap
````
kubectl apply -f configmap.yaml
kubectl delete -f configmap.yaml
````