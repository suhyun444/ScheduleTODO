FROM openjdk:21
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY ./scheduletodo/build/libs/scheduletodo-0.0.1-SNAPSHOT.jar scheduletodo.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar scheduletodo.jar
