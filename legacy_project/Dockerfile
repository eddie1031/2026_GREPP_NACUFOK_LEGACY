FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /build

COPY pom.xml .
COPY lib ./lib
COPY src ./src

RUN mvn -B -DskipTests clean package

FROM tomcat:9.0-jdk8-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /build/target/dpst.war /usr/local/tomcat/webapps/dpst.war
COPY layout /app/dpst/layout

EXPOSE 8080

CMD ["catalina.sh", "run"]
