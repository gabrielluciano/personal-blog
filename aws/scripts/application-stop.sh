#!/bin/bash
set -xe

cd /app/personal-blog

[ -f /app/personal-blog/docker-compose.yml ] && docker compose down
