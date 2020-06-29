FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/gogomogolo/assignment.git

FROM maven:3.6.3-openjdk-14-slim
WORKDIR /app
COPY --from=0 /app/assignment /app

RUN mvn install
FROM openjdk:14-jdk-slim
WORKDIR /app
COPY --from=1 /app/target/courier-tracker-0.0.1-SNAPSHOT.jar /app
EXPOSE 5000
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar courier-tracker-0.0.1-SNAPSHOT.jar"]