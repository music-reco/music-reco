FROM openjdk:21
ARG JAR_FILE=/build/libs/*.jar
RUN mkdir -p /app/temp
VOLUME ["/app/temp"]
RUN chmod 777 /app/temp
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod" ,"/app.jar"]
