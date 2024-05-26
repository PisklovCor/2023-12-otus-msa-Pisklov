## Приложение резервирует товар

Приложение отвечает на порут 8003

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-08-store-service-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw08-store-service -p 8003:8003 -e logging.level.root='DEBUG' -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' -e spring.artemis.broker-url='tcp://jms-broker:61616' --network=hw-networks -d pisklovcor/hw-08-store-service-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````