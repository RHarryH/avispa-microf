FROM eclipse-temurin:17.0.9_9-jre-alpine

MAINTAINER rafalhiszpanski.pl

ENV AVISPA_ECM_FILE_STORE_PATH="/home/microf/default-file-store"
ENV AVISPA_ECM_FILE_STORE_NAME="default-file-store"
ENV REMOTE_DEBUG=0
ENV REMOTE_DEBUG_PORT=5005
ARG MICROF_DIR="/opt/microf"
ARG EXTRACTED_DIR="target/extracted"

WORKDIR ${MICROF_DIR}

# install libreoffice and Liberation font family
RUN apk --update --upgrade --no-cache add libreoffice
RUN apk --update --upgrade --no-cache add ttf-liberation

# create logs directory
RUN mkdir -p logs

RUN addgroup -S microf && adduser -S microf -G microf
USER microf

VOLUME /tmp

COPY docker-run.sh .
COPY ${EXTRACTED_DIR}/BOOT-INF/lib lib
COPY ${EXTRACTED_DIR}/META-INF META-INF
COPY ${EXTRACTED_DIR}/BOOT-INF/classes .
ENTRYPOINT ["sh", "docker-run.sh"]