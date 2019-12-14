FROM maven:3.6.1-jdk-8-alpine
VOLUME /tmp
ADD /target/actuator-sample-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]