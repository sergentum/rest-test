#!/bin/bash

cp -f ../build/libs/rest-test-1.0.0.jar rest-test-1.0.0.jar
docker build -t sergentum/rest-test .
