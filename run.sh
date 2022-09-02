#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" ; pwd -P)"

_usage() {
    cat <<EOF
Usage: $0 command
commands:
  build-jar                                            Build jar file
  docker-build                                         Build docker image
  docker-run                                           Run docker container
EOF
  exit 1
}

CMD=${1:-}
shift || true
case ${CMD} in
  docker-run)
    docker run -p 8080:8080 challenge &
    ;;
  docker-build)
    ./gradlew clean bootJar
    docker build --build-arg JAR_FILE=build/libs/\*.jar -t challenge .
    ;;
  build-jar)
    ./gradlew clean bootJar
    ;;
  *) _usage ;;
esac