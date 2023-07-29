#!/bin/bash
set -xe

cd /app/personal-blog

aws s3 cp "s3://$PROD_S3_BUCKET/Dockerfile" ./Dockerfile
aws s3 cp "s3://$PROD_S3_BUCKET/docker-compose.yml" ./docker-compose.yml
aws s3 cp "s3://$PROD_S3_BUCKET/app.jar" ./target/app.jar
