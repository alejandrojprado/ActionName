FROM openjdk:11.0.4-jre
VOLUME /tmp
COPY ms-search-service-ng/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
