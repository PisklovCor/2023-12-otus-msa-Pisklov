## Манифесты для Apigateway

Приложение отвечает по хосту [arch.homework](http://arch.homework)

### Директория домашней работы с манифестами k8s
```shell
cd hw06/manifest
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

### Применить манифесты одной командой kubectl
```shell
kubectl apply -f .
```

### Удаление всего окруженяи после завершения работы
```shell
minikube delete --all
```