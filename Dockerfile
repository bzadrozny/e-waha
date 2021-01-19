FROM openjdk:11
ADD target/e-waha-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar e-waha-0.0.1-SNAPSHOT.jar