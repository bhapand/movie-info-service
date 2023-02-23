FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} movie-info-service.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","/movie-info-service.jar"]
