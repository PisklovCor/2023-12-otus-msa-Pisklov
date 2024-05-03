## Манифесты описывают сущности: Deployment, Service, Ingress

Приложение отвечает на порут 8000 

### Проверка 
````
curl http://arch.homework/health
curl http://arch.homework/rewrite/health
````

### Создание kind: Deployment
````
kubectl apply -f deployment.yaml
````

### Создание kind: Service
````
kubectl apply -f service.yaml
````

### Создание kind: Ingress
````
kubectl apply -f ingress.yaml
````

### Применить манифесты одной командой kubectl
````
kubectl apply -f .
````

### Команды взаимодействия (minikube, kubectl)
````
minikube dashboard

minikube addons enable ingress

minikube tunnel

pods/replicaset/deploy/svc/ingress
kubectl get 
kubectl describe 
````