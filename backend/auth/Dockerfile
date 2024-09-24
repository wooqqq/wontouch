# build
FROM amazoncorretto:17-alpine AS build

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
RUN ./gradlew build --no-daemon

# runtime
FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/build/libs/auth-0.0.1-SNAPSHOT.jar /app/wontouch-auth.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/wontouch-auth.jar"]