FROM node:20-alpine as frontend-builder
WORKDIR ./
COPY ./javinukai-front/package.json .
RUN npm i
COPY ./javinukai-front .
RUN npm run build

FROM maven:3.8.4-openjdk-17 as backend-builder
COPY javinukai-back/src /app/src
COPY javinukai-back/pom.xml /app
COPY --from=frontend-builder ./dist /app/src/main/resources/static
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM tomcat:10.1.20-jdk17-temurin-jammy
COPY --from=backend-builder app/target/*.war /usr/local/tomcat/webapps/
EXPOSE 8080
ENTRYPOINT ["catalina.sh", "run"]