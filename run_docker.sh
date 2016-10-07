#!/bin/sh
COMMIT="$(git rev-parse HEAD)"
PORT=${1-80}
SCALE=${2-2}
echo $PORT
export COMMIT
export PORT
docker-compose up -d
docker-compose scale blogapp=$SCALE
docker-compose logs -f