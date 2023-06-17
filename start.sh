#!/bin/bash

docker-compose up -d

docker-compose -f zk-single-kafka-single.yml up -d
