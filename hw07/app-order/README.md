## Приложение оформление заказов

Приложение отвечает на порут 8002

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-07-app-order-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw07-order -p 8002:8002 -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' --network=hw-networks -d pisklovcor/hw-07-app-order-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````