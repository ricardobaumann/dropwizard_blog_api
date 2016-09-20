#!/bin/sh
COMMIT="$(git rev-parse HEAD)"
export COMMIT
docker-compose scale blogapp=$1