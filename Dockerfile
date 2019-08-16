FROM openjdk:12

COPY build/libs/fsrss.jar fsrss.jar

RUN useradd fsrss
USER fsrss

CMD java -jar fsrss.jar
