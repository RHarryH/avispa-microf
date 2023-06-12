#!/bin/sh

if [ "$REMOTE_DEBUG" == "1" ]; then
  REMOTE_DEBUG_STRING=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${REMOTE_DEBUG_PORT}
else
  REMOTE_DEBUG_STRING=""
fi

exec java -Dspring.profiles.active=prod \
  -Dspring.datasource.user=${DATASOURCE_USERNAME} \
  -Dspring.datasource.password=${DATASOURCE_PASSWORD} \
  -Davispa.ecm.file-store.path=${AVISPA_ECM_FILE_STORE_PATH} \
  -Davispa.ecm.file-store.name=${AVISPA_ECM_FILE_STORE_NAME} \
  -jar ${REMOTE_DEBUG_STRING} microf.jar