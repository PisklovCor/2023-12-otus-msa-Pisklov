## Приложение миграции liquibase

https://habr.com/ru/articles/578744/

### Локальный запуск postgres:
````
cd .\hw04\application-hw04\
docker-compose up -d
````

### Запуск миграции
````
mvn liquibase:update -Pdocker
````

### Сборка образа
````
docker build -t pisklovcor/hw-04-migration-docker:dockerfile .  
````

### Запуск образа
````
docker run pisklovcor/hw-04-migration-docker:dockerfile
````