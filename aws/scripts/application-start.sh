#!/bin/bash
set -xe

cd /app/personal-blog

docker compose -f docker-compose.prod.yml up -d --build
