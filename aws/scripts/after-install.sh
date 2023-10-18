#!/bin/bash
set -xe

cd /app/personal-blog

aws s3 cp "s3://$PROD_S3_BUCKET/bundle.tar.gz" ./bundle.tar.gz

tar -zxvf bundle.tar.gz

rm -rf bundle.tar.gz
