## Приложение с помощью Spring Boot

Приложение отвечает на порут 8000 

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````
docker build -t pisklovcor/hw-02-docker:dockerfile .
````

### Запуск docker образа:
````
docker run -p 8000:8000 -d pisklovcor/hw-02-docker:dockerfile
````