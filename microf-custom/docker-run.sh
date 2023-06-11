#!/bin/sh

if [ "$DEBUG" == "1" ]; then
  DEBUG_STRING=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT}
else
  DEBUG_STRING=""
fi

exec java -Dspring.profiles.active=prod \
  -Dspring.datasource.user=${DATASOURCE_USERNAME} \
  -Dspring.datasource.password=${DATASOURCE_PASSWORD} \
  -Davispa.ecm.file-store.path=${AVISPA_ECM_FILE_STORE_PATH} \
  -Davispa.ecm.file-store.name=${AVISPA_ECM_FILE_STORE_NAME} \
  -jar ${DEBUG_STRING} microf.jar