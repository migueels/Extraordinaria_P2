FROM openjdk:15-jdk-alpine
COPY "Superhereoes_JSON.json" "Superhereoes_JSON.json"
COPY "./target/Practica2-1.0-Extraordinaria.jar" "app.jar"
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]