FROM maven:3.6.3-openjdk-14-slim AS build
WORKDIR /build
# copy just pom.xml (dependencies and dowload them all for offline access later - cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B
# copy source files and compile them (.dockerignore should handle what to copy)
COPY . .
RUN mvn package -DskipTests


# Runnable image
FROM openjdk:12-alpine as runnable
VOLUME /tmp
VOLUME /logs


ARG JAR_FOLDER=/build/target

# Copy the jar file
COPY --from=build ${JAR_FOLDER}/log-parser.jar log-parser.jar

EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "/log-parser.jar"]
