FROM maven:3.9.10-eclipse-temurin-24 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM tomcat:11.0.22-jdk25-temurin-noble
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]