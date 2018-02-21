FROM openjdk:8-jre-alpine
COPY ./mimic-web/build/libs/mimic-web-1.0.0-SNAPSHOT.jar /opt/mimic/
WORKDIR /opt/mimic
EXPOSE 80
EXPOSE 443
CMD ["java", "-jar", "mimic-web-1.0.0-SNAPSHOT.jar"]