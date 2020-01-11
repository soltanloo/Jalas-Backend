#FROM maven:3.6.1-jdk-8-alpine
#FROM mcr.microsoft.com/java/jdk:13u1-zulu-alpine
#COPY target/Jalas-Backend.war /usr/app/
#WORKDIR /usr/app
#ENTRYPOINT ["java","-jar","Jalas-Backend.war"]

FROM tomcat:9.0.30-jdk13-openjdk-oracle
COPY target/Jalas-Backend.war /usr/local/tomcat/webapps/
#VOLUME /tmp
#ADD /target/actuator-sample-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]