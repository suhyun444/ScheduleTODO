#!/bin/bash
cd ./scheduletodo
./gradlew clean build || exit 1
cd ..
docker-compose build --no-cache || exit 1
docker-compose up -d