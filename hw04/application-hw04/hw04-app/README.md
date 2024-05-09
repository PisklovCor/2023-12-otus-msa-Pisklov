## Приложение с помощью Spring Boot (RESTful CRUD)

Приложение отвечает на порут 8000

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````
mvn clean package
docker build -t pisklovcor/hw-04-app-pro-docker:dockerfile .
````

### Запуск docker образа:
````
docker run --name hw04-app -p 8000:8000 -d pisklovcor/hw-04-app-pro-docker:dockerfile
````

### Проверка network:
````
docker network ls
````

### Сборка метрик и мониторинг (запуск docker образа с переменными):
Реализация: [Сбор метрик Spring Boot приложения c помощью Prometheus и Grafana](https://habr.com/ru/articles/548700/)
Пример: [spring-metrics](https://github.com/kirya522/medium-posts/tree/main/java/spring-metrics)
Замена DockerFile c JAVA_OPTS: [spring-boot-docker](https://spring.io/guides/topicals/spring-boot-docker)
````
docker run --name hw04-app -p 8000:8000 -e "JAVA_OPTS=-Ddebug -Dspring.datasource.url=jdbc:postgresql://postgres:5432/postgres" --network=hw-networks -d pisklovcor/hw-04-app-pro-docker:dockerfile
````