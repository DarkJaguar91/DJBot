#!/bin/bash

cd ../
./gradlew build

rc=$?; if [[ ${rc} != 0 ]]; then exit ${rc}; fi

cp ./docker/* ./build/distributions/
cd ./build/distributions/

sed -i "s/TOKEN/$1/g" Dockerfile

sudo docker-compose build
sudo docker-compose up