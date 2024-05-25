## Приложение оформление заказов

Приложение отвечает на порут 8002

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-08-payment-service-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw08-payment-service -p 8002:8002 -e logging.level.root='DEBUG' -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' -e spring.artemis.broker-url='tcp://jms-broker:61616' --network=hw-networks -d pisklovcor/hw-08-payment-service-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````