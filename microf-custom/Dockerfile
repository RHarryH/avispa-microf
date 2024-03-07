FROM eclipse-temurin:17.0.9_9-jre-alpine AS builder
WORKDIR microf
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} microf.jar
RUN java -Djarmode=layertools -jar microf.jar extract

FROM eclipse-temurin:17.0.9_9-jre-alpine

LABEL org.opencontainers.image.authors="rafalhiszpanski.pl"

ENV AVISPA_ECM_FILE_STORE_PATH="/home/microf/default-file-store"
ENV AVISPA_ECM_FILE_STORE_NAME="default-file-store"
ENV REMOTE_DEBUG=0
ENV REMOTE_DEBUG_PORT=5005
ARG MICROF_DIR="/opt/microf"

WORKDIR ${MICROF_DIR}

# install libreoffice and Liberation font family
RUN apk --update --upgrade --no-cache add libreoffice
RUN apk --update --upgrade --no-cache add ttf-liberation

# create logs directory
RUN mkdir -p logs

RUN addgroup -S microf && adduser -S microf -G microf
USER microf

VOLUME /tmp

ENV LANG pl_PL.UTF-8
ENV LANGUAGE pl_PL.UTF-8
ENV LC_ALL pl_PL.UTF-8

COPY docker-run.sh .
COPY --from=builder microf/dependencies/ ./
COPY --from=builder microf/spring-boot-loader/ ./
COPY --from=builder microf/snapshot-dependencies/ ./
COPY --from=builder microf/application/ ./
ENTRYPOINT ["sh", "docker-run.sh"]