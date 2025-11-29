FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
ADD target/embula-backend.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
