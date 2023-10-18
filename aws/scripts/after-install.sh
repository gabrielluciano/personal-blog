#!/bin/bash
set -xe

# cleanup dangling images so we don't end up with full disk
docker image prune --force
