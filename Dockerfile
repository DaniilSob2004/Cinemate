FROM openjdk:17-jdk-slim
WORKDIR /app
COPY src/main/resources/data/ /app/data/
COPY src/main/resources/db_data/ /app/db_data/
COPY target/cinemate-0.0.1-SNAPSHOT.jar /app/cinemate.jar
ENTRYPOINT ["java", "-jar", "cinemate.jar", "--spring.profiles.active=docker"]