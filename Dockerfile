FROM amazoncorretto:17.0.3-alpine3.15
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ./build/libs/challenge-0.0.1.jar /app/
WORKDIR /app

CMD ["java", "-jar", "challenge-0.0.1.jar"]