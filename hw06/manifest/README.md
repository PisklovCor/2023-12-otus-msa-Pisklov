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
```
minikube tunnel
```

### dashboard-отдельная консоль
```
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

### Для тестирования коллекции postman включить настройку:
```
Settings:
    Automatically follow redirects
    Follow HTTP 3xx responses as redirects.
```