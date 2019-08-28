#!/usr/bin/env sh
./gradlew clean build
docker image build -t workout-rival-service .