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
docker build -t pisklovcor/hw04-liquibase-migration-docker:dockerfile .  
````

### Запуск образа
````
docker run --name hw04-liquibase-migration --network=hw-networks pisklovcor/hw04-liquibase-migration-docker:dockerfile --defaultsFile=liquibase.docker.properties  --changeLogFile=db.changelog-master.yaml update
docker run --name hw04-liquibase-migration --network=hw-networks pisklovcor/hw04-liquibase-migration-docker:dockerfile --defaultsFile=liquibase.docker.properties  update
docker run --name hw04-liquibase-migration --network=hw-networks -v /resources/:/liquibase/changelog pisklovcor/hw04-liquibase-migration-docker:dockerfile --changeLogFile=resources/db/changelog/db.changelog-master.yaml --url=jdbc:postgresql://postgres:5432/postgres --username=admin --password=admin update
````

### Проверка network:
````
docker network ls
````