FROM eclipse-temurin:21
LABEL authors="Jack Arana Ramos"

COPY ./target/Enlace-0.0.1-SNAPSHOT.jar /

RUN chmod +x /Enlace-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch Enlace-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","/Enlace-0.0.1-SNAPSHOT.jar"]
