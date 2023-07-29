#!/bin/bash
set -xe

cd /app/personal-blog

docker build -t personalblog .

docker compose up -d
