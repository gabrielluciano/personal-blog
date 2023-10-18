#!/bin/bash
set -xe

cd /app/personal-blog

# cleanup dangling images so we don't end up with full disk
docker image prune --force
