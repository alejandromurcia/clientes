FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /tmp
COPY /target/clientes-0.0.1-SNAPSHOT.jar catalogo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","clientes-0.0.1-SNAPSHOT.jar"]