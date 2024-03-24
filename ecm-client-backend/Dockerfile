FROM eclipse-temurin:17.0.10_7-jre-alpine AS builder
WORKDIR ecm-client
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ecm-client-backend.jar
RUN java -Djarmode=layertools -jar ecm-client-backend.jar extract

FROM eclipse-temurin:17.0.10_7-jre-alpine

LABEL org.opencontainers.image.authors="rafalhiszpanski.pl"

ENV AVISPA_ECM_FILE_STORE_PATH="/home/ecm-client/default-file-store"
ENV AVISPA_ECM_FILE_STORE_NAME="default-file-store"
ENV REMOTE_DEBUG=0
ENV REMOTE_DEBUG_PORT=5005
ARG CLIENT_DIR="/opt/ecm-client"

WORKDIR ${CLIENT_DIR}

# install libreoffice and Liberation font family
RUN apk --update --upgrade --no-cache add libreoffice
RUN apk --update --upgrade --no-cache add ttf-liberation

# create logs directory
RUN mkdir -p logs

RUN addgroup -S ecm-client && adduser -S ecm-client -G ecm-client
USER ecm-client

VOLUME /tmp

ENV LANG pl_PL.UTF-8
ENV LANGUAGE pl_PL.UTF-8
ENV LC_ALL pl_PL.UTF-8

COPY docker-run.sh .
COPY --from=builder ecm-client/dependencies/ ./
COPY --from=builder ecm-client/spring-boot-loader/ ./
COPY --from=builder ecm-client/snapshot-dependencies/ ./
COPY --from=builder ecm-client/application/ ./
ENTRYPOINT ["sh", "docker-run.sh"]
#ENTRYPOINT ["top"]