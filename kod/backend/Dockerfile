FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /home/app
COPY pom.xml pom.xml
COPY ./src ./src
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:21-ea-17-slim-buster
WORKDIR /home/app
COPY --from=builder /home/app/target/management-0.0.1-SNAPSHOT.jar ./management-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "management-0.0.1-SNAPSHOT.jar"]