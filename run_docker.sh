#!/bin/sh
COMMIT="$(git rev-parse HEAD)"
PORT=$1
echo $PORT
export COMMIT
export PORT
docker-compose up -d