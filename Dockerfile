FROM openjdk:21

WORKDIR /app

COPY target/transaction_management-1.0-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "transaction_management-1.0-SNAPSHOT.jar"]