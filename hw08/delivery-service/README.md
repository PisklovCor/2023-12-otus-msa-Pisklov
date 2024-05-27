## Приложение организует доставку

Приложение отвечает на порут 8004

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-08-delivery-service-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw08-delivery-service -p 8004:8004 -e logging.level.root='DEBUG' -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' -e spring.artemis.broker-url='tcp://jms-broker:61616' --network=hw-networks -d pisklovcor/hw-08-delivery-service-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````