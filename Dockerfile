FROM --platform=linux/amd64 amazoncorretto:11-alpine

COPY tippspiel2-spring/target/tippspiel2-spring-*.jar app.jar
ENTRYPOINT ["java","-Xmx512m","-Xms512m","-XX:+UseContainerSupport","-jar","/app.jar"]

