#!/bin/bash
set -xe

cd /app/personal-blog

# if the docker compose file exists stop the application
[ -f /app/personal-blog/docker-compose.prod.yml ] && docker compose -f docker-compose.prod.yml down
