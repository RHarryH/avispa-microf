FROM avispa/ecm-client-backend:2.2.0

LABEL org.opencontainers.image.authors="rafalhiszpanski.pl"

COPY docker-run.sh .
COPY --chmod=644 target/microf-custom ./BOOT-INF/lib

ENTRYPOINT ["sh", "docker-run.sh"]