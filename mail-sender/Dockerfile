FROM maven:3.8.5-openjdk-17 AS build-mail
WORKDIR /
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package

FROM openjdk:17-jdk-slim
WORKDIR /
COPY /src /src
COPY --from=build-mail /target/*.jar mail-sender.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "mail-sender.jar"]