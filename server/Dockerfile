# semi parking demo — Railway용 (non-root 실행: 내장PG initdb가 root를 거부하므로)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN sh mvnw -q dependency:go-offline
COPY src ./src
RUN sh mvnw -q package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
RUN groupadd -r appuser && useradd -r -g appuser appuser \
  && mkdir -p /home/appuser/.embedpostgresql /tmp/epg \
  && chown -R appuser:appuser /app /home/appuser /tmp/epg
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
