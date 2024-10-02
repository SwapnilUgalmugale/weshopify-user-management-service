FROM openjdk:17-alpine

ARG WORK_DIR=/opt/categories-service
ARG ARTIFACT_NAME=weshopify-categories-service.jar
ARG SERVICE_PORT=5016

ENV FINAL_ARTIFACT=${ARTIFACT_NAME}

RUN mkdir ${WORK_DIR}

WORKDIR ${WORK_DIR} 

COPY target/${ARTIFACT_NAME} ${WORK_DIR}

EXPOSE ${SERVICE_PORT}

CMD [ "sh","-c","java -jar ${FINAL_ARTIFACT}"]