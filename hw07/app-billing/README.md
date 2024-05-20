## Приложение для биллинга

Приложение отвечает на порут 8001

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-07-app-billing-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw07-billing -p 8001:8001 -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' --network=hw-networks -d pisklovcor/hw-07-app-billing-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````