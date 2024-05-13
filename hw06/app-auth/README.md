## Приложение авторизации/аутентификации

Приложение отвечает на порут 8001

### Сборка проекта:
````
mvn clean package
````

### Сборка docker images:
````shell
docker build -t pisklovcor/hw-06-auth-docker:dockerfile .
````

### Запуск docker образа:
````shell
docker run --name hw06-auth -p 8001:8001 --network=hw-networks -e "JAVA_OPTS=-Ddebug -Dspring.datasource.url=jdbc:postgresql://postgres:5432/postgres" -d pisklovcor/hw-06-auth-docker:dockerfile
````

### Проверка network:
````shell
docker network ls
````