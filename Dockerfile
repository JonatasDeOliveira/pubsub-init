FROM google/cloud-sdk:313.0.1

ARG JAR_FILE=build/libs/*.jar
ARG SH_FILE=./*.sh

COPY ${JAR_FILE} app.jar
COPY ${SH_FILE} start.sh

EXPOSE 8086

ENTRYPOINT ["bash", "start.sh"]