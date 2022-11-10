FROM openjdk:8-jdk-alpine

COPY build/libs/tournament-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4346

ENTRYPOINT ["java", "-Dspring.profiles.active=k8n",  "-jar", "/app.jar"]
