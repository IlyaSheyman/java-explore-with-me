FROM amazoncorretto:11-alpine-jdk
WORKDIR /app
COPY target/stats-server.jar /app/stats-server.jar
CMD ["java", "-jar", "stats-server.jar"]