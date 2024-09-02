FROM amazoncorretto:17
ENV TZ="Europe/Samara"
COPY target/*.jar bot-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/bot-0.0.1.jar"]