FROM maven:3.6.3-jdk-13 as BUILD
COPY src /usr/src/jalas/src
COPY pom.xml /usr/src/jalas
RUN mvn -f /usr/src/jalas/pom.xml clean package

FROM tomcat:9.0.30-jdk13-openjdk-oracle
COPY --from=BUILD /usr/src/jalas/target/Jalas-Backend.war /usr/local/tomcat/webapps/