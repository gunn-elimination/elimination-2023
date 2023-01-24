FROM gradle:jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon --stacktrace

FROM gradle:jdk17 AS runtime
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 80
ENV PORT=80
CMD ["java", "-jar", "app.jar"]