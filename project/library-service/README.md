## Приложение просмотра и получения книг

Приложение отвечает на порут 8000

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/project-library-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name project-library -p 8000:8000 -e logging.level.root='DEBUG' -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' -e application.order-url='http://host.docker.internal:8002' -e application.destinationSend='library-notice' -e spring.artemis.broker-url='tcp://jms-broker:61616'--network=project-networks -d pisklovcor/project-library-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````