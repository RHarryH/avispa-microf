FROM adoptopenjdk/openjdk11:jre-11.0.19_7-alpine

MAINTAINER rafalhiszpanski.pl

ENV AVISPA_ECM_FILE_STORE_PATH="/home/microf/default-file-store"
ENV AVISPA_ECM_FILE_STORE_NAME="default-file-store"
ENV REMOTE_DEBUG=0
ENV REMOTE_DEBUG_PORT=5005
ARG MICROF_PATH="/opt/microf"

WORKDIR ${MICROF_PATH}

# install libreoffice and Liberation font family
RUN apk --update --upgrade --no-cache add libreoffice
RUN apk --update --upgrade --no-cache add ttf-liberation

# create logs directory
RUN mkdir -p logs

RUN addgroup -S microf && adduser -S microf -G microf
USER microf

VOLUME /tmp

COPY docker-run.sh .
COPY target/*.jar microf.jar
ENTRYPOINT ["sh", "docker-run.sh"]