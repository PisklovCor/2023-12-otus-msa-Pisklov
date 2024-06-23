## Сервис отправки уведомлений

Приложение отвечает на порут 8003

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/project-notification-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name project-notification -p 8003:8003 -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' -e application.destinationListener='library-notice' -e spring.artemis.broker-url='tcp://jms-broker:61616' --network=hw-networks -d pisklovcor/project-notification-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````