#!/bin/sh

cd ../
./gradlew build

rc=$?; if [[ ${rc} != 0 ]]; then exit ${rc}; fi

cp ./docker/* ./build/distributions/
cd ./build/distributions/

sed 's/\$TOKEN\$/$1/g' Dockerfile

docker-compose build
docker-compose up