#!/bin/bash

set -e  # Exit on any error

# Read current version from pom.xml
CURRENT_VERSION=$(xmllint --xpath "/*[local-name()='project']/*[local-name()='version']/text()" ../pom.xml)
echo "Current version: $CURRENT_VERSION"

# Get bump type from user input (default: patch)
BUMP_TYPE=${1:-patch}  # Usage: ./build-and-push-backend.sh [patch|minor|major]

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

# Update version in pom.xml
sed -i.bak "s|<version>${CURRENT_VERSION}</version>|<version>${NEW_VERSION}</version>|" ../pom.xml

BASE_IMAGE_NAME="share-food-backend"
IMAGE_NAME="${BASE_IMAGE_NAME}:${NEW_VERSION}"

# Step 1: Build images for both platforms
echo "Building backend images..."
podman build --platform linux/arm64 -t "${IMAGE_NAME}-arm64" ..
podman build --platform linux/amd64 -t "${IMAGE_NAME}-amd64" ..

# Step 2: Create and push manifest
echo "Creating and pushing manifest..."
podman manifest create "$IMAGE_NAME"

podman manifest add "$IMAGE_NAME" "${IMAGE_NAME}-amd64"
podman manifest add "$IMAGE_NAME" "${IMAGE_NAME}-arm64"

podman manifest push "$IMAGE_NAME" "docker.io/andreigs28/${IMAGE_NAME}"

# Step 3: Clean up pom.xml backup
rm ../pom.xml.bak

echo "Done. Backend new version: $NEW_VERSION"

sed -i.bak "s|share-food-backend:${CURRENT_VERSION}|share-food-backend:${NEW_VERSION}|" ../../docker-compose.yaml
rm ../../docker-compose.yaml.bak