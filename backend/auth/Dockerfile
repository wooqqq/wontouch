# runtime
FROM amazoncorretto:17-alpine AS build

WORKDIR /app

COPY --from=build /build/libs/auth-0.0.1-SNAPSHOT.jar /app/wontouch-auth.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/wontouch-auth.jar"]