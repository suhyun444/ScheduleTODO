FROM openjdk:21
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY build/libs/ScheduleTODO-0.0.1-SNAPSHOT.jar scheduletodo.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar scheduletodo.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar scheduletodo.jar
