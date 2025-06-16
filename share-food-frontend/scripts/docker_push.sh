#!/bin/bash

set -e  # Exit on any error

# Paths and values
CONFIG_FILE="../src/app/config/api-config.ts"
LOCAL_URL="http://localhost:8080"
PROD_URL="https://www.foodtech.pw"

BASE_IMAGE_NAME="share-food-frontend"

# Read current version from package.json
CURRENT_VERSION=$(grep '"version":' ../package.json | sed 's/[^0-9.]*\([0-9.]*\).*/\1/')
echo "Current version: $CURRENT_VERSION"

# Get bump type from user input (default: patch)
BUMP_TYPE=${1:-patch}  # Usage: ./build-and-push.sh [patch|minor|major]

bump_version() {
    local version=$1
    local bump=$2
    local major minor patch

    IFS='.' read -r major minor patch <<< "$version"

    case $bump in
        major)
            major=$((major + 1))
            minor=0
            patch=0
            ;;
        minor)
            minor=$((minor + 1))
            patch=0
            ;;
        patch|*)
            patch=$((patch + 1))
            ;;
    esac
    echo "${major}.${minor}.${patch}"
}

# Calculate new version
NEW_VERSION=$(bump_version "$CURRENT_VERSION" "$BUMP_TYPE")
echo "Bumping version: $CURRENT_VERSION -> $NEW_VERSION"

# Update package.json version
sed -i.bak "s/\"version\": \"$CURRENT_VERSION\"/\"version\": \"$NEW_VERSION\"/" ../package.json

IMAGE_NAME="${BASE_IMAGE_NAME}:${NEW_VERSION}"

# Step 1: Replace localhost with production URL
echo "Replacing $LOCAL_URL with $PROD_URL in $CONFIG_FILE..."
sed -i.bak "s|$LOCAL_URL|$PROD_URL|g" "$CONFIG_FILE"

# Step 2: Build images for both platforms
echo "Building images..."
podman build --platform linux/arm64 -t "${IMAGE_NAME}-arm64" ..
podman build --platform linux/amd64 -t "${IMAGE_NAME}-amd64" ..

# Step 3: Create and push manifest
echo "Creating and pushing manifest..."
podman manifest create "$IMAGE_NAME"

podman manifest add "$IMAGE_NAME" "${IMAGE_NAME}-amd64"
podman manifest add "$IMAGE_NAME" "${IMAGE_NAME}-arm64"

podman manifest push "$IMAGE_NAME" "402289717488.dkr.ecr.eu-north-1.amazonaws.com/share-food/${IMAGE_NAME}"

# Step 4: Revert api-config.ts to original
echo "Reverting $CONFIG_FILE to original..."
mv "${CONFIG_FILE}.bak" "$CONFIG_FILE"

# Step 5: Clean up package.json backup
rm ../package.json.bak

echo "Done. New version: $NEW_VERSION"

sed -i.bak "s|share-food-frontend:${CURRENT_VERSION}|share-food-frontend:${NEW_VERSION}|" ../../docker-compose.yaml
rm ../../docker-compose.yaml.bak
