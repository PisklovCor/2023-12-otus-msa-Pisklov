## Манифесты описывают сущности: Deployment, Service, Ingress, Secret

Приложение отвечает по хосту [arch.homework](http://arch.homework)

### Проверка curl
```shell
curl http://arch.homework/health
```

### [swagger-ui](http://arch.homework/swagger-ui/index.html)

### [prometheus](http://localhost:9090/)

### Директория домашней работы с манифестами k8s
```shell
cd hw05/manifest
```

### Команды взаимодействия (minikube, kubectl), подготовка окружения
```shell
minikube start
```

```shell
minikube addons enable ingress
```

### tunnel-отдельная консоль
```shell
minikube tunnel
```

### dashboard-отдельная консоль
```shell
minikube dashboard
```

### Команды взаимодействия (helm), подготовка окружения

```shell 1
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
```

```shell
helm repo update
```

```shell
helm repo list
```

```shell
helm list
```

```shell 
helm install stack prometheus-community/kube-prometheus-stack -f c:\JavaProject\otus\2023-12-otus-msa-Pisklov\hw05\config\prometheus.yaml
```

### prometheus-отдельная консоль
```shell 
kubectl port-forward service/prometheus-operated  9090
```

### grafana-отдельная консоль (admin/prom-operator)
```shell 
kubectl port-forward service/stack-grafana 9000:80
```


### Применить манифесты одной командой kubectl
```shell
kubectl apply -f .
```

### Удаление всего окруженяи после завершения работы
```shell
minikube delete --all
```