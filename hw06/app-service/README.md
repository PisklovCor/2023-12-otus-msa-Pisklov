## Приложение пользователей

Приложение отвечает на порут 8001

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-06-service-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw06-service -p 8002:8002 -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' --network=hw-networks -d pisklovcor/hw-06-service-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````