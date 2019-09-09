FROM openjdk:12

COPY build/libs/fsrss.jar fsrss.jar

RUN useradd fsrss
RUN mkdir /data && chown fsrss:root /data
USER fsrss

CMD java -jar fsrss.jar
