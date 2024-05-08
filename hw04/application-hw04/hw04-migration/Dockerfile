FROM maven:3.6.3-openjdk-17

RUN mkdir hw-04-migration

WORKDIR hw-04-migration

COPY . .

ENV JDBC_URL="jdbc:postgresql://127.0.0.1:5432/postgres" \
  JDBC_USERNAME="admin" \
  JDBC_PASSWORD="admin"

CMD ["mvn", "liquibase:update", "-Pdocker", "-e"]