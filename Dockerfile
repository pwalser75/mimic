FROM openjdk:8-jre-alpine
COPY ./build/libs/mimic-1.0.0-SNAPSHOT.jar /opt/mimic/
WORKDIR /opt/mimic
EXPOSE 80
EXPOSE 443
CMD ["java", "-jar", "mimic-1.0.0-SNAPSHOT.jar"]