#!/bin/bash

if [[ -z "${PUBSUB_EMULATOR_HOST}" ]]; then
  echo "No PUBSUB_EMULATOR_HOST supplied, setting default of localhost:8086"
  export PUBSUB_EMULATOR_HOST=localhost:8086
fi

if [[ -z "${PUBSUB_PROJECT}" ]]; then
  echo "No PUBSUB_PROJECT supplied, setting default of docker-gcp-project"
  export PUBSUB_PROJECT=docker-project
fi

if [[ -z "${PUBSUB_TOPIC}" ]]; then
  echo "No PUBSUB_TOPIC supplied, setting default of docker-topic"
  export PUBSUB_TOPIC=docker-topic
fi

if [[ -z "${PUBSUB_SUBSCRIPTION}" ]]; then
  echo "No PUBSUB_SUBSCRIPTION supplied, setting default of docker-subscription"
  export PUBSUB_SUBSCRIPTION=docker-subscription
fi

gcloud beta emulators pubsub start --host-port=0.0.0.0:8086 --project=${PUBSUB_PROJECT} &
PUBSUB_PID=$!

echo "Creating topics and subscriptions"
java -jar app.jar

echo "Pubsub Ready"
wait ${PUBSUB_PID}