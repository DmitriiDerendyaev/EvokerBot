FROM amazoncorretto:17
ENV TZ="Europe/Samara" SPRING_PROFILES_ACTIVE=local
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]