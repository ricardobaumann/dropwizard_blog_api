#!/bin/sh
COMMIT="$(git rev-parse HEAD)"
PORT=80
echo $PORT
export COMMIT
export PORT
docker-compose down