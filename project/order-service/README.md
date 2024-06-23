## Сервис заявок на книги

Приложение отвечает на порут 8002

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/project-order-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name project-order -p 8002:8002 -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' --network=project-networks -d pisklovcor/project-order-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````