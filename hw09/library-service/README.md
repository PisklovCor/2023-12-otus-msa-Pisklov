## Приложение просмотра и получения книг

Приложение отвечает на порут 8000

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-09-library-service-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw09-library-service -p 8000:8000 -e logging.level.root='DEBUG' -e spring.datasource.url='jdbc:postgresql://postgres:5432/postgres' --network=hw-networks -d pisklovcor/hw-09-library-service-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````