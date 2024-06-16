## Приложение авторизации/аутентификации

Приложение отвечает на порут 8001

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/project-account-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name project-account -p 8001:8001 -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' --network=project-networks -d pisklovcor/project-account-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````