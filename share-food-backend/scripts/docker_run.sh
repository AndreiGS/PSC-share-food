#!/bin/sh

podman rm image --force share-food-app
podman buildx build --platform linux/arm64 -t share-food-app --load ..
podman run --name=share-food-app -p 8080:8080 -p 4848:4848 \
  --env-file .env \
  -e AWS_REGION=eu-north-1 \
  -e DB_SECRET_NAME=dev/share-food/MySql \
  -e DB_CRED_SECRET_NAME=rds!db-f776fd7a-a56b-4ff1-8f9f-1d8b7af165f2 \
  -e ADMIN_SECRET_NAME=dev/share-food/Admin \
  share-food-app
