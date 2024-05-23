## Приложение оформление заказов

Приложение отвечает на порут 8001

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-08-order-service-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw08-order-service -p 8001:8001 -e logging.level.root='DEBUG' -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' -e destinationSend='order-payment' -e destinationListener='payment-order' -e spring.artemis.broker-url='tcp://jms-broker:61616' --network=hw-networks -d pisklovcor/hw-08-order-service-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````