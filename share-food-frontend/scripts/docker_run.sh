#!/bin/sh

podman rm image --force share-food-frontend
podman buildx build --platform linux/arm64 -t share-food-frontend --load ..
podman run --name=share-food-frontend -p 4200:80 share-food-frontend
