## Приложение создания уведомлений

Приложение отвечает на порут 8003

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-07-app-notice-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw07-notice -p 8003:8003 -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' --network=hw-networks -d pisklovcor/hw-07-app-notice-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````