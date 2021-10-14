FROM java:8
MAINTAINER sanelee <18710731037@163.com>
VOLUME /tmp
ADD kafka-producer-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
