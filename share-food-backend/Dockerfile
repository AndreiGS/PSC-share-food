# Build stage
FROM maven:3.9.9-amazoncorretto-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY mvnw mvnw.cmd ./
#COPY .mvn ./.mvn
COPY src ./src
RUN mvn clean package -DskipTests

# Final stage
FROM payara/server-full:6.2025.5-jdk17

# Switch to root to install packages
USER root

# Install required packages
RUN apt-get update && \
    apt-get install -y curl unzip jq && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install AWS CLI
ARG TARGETARCH
RUN if [ "$TARGETARCH" = "amd64" ]; then \
      curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"; \
    elif [ "$TARGETARCH" = "arm64" ]; then \
      curl "https://awscli.amazonaws.com/awscli-exe-linux-aarch64.zip" -o "awscliv2.zip"; \
    else \
      echo "Unsupported architecture $TARGETARCH" && exit 1; \
    fi && \
    unzip awscliv2.zip && \
    ./aws/install && \
    rm -rf aws awscliv2.zip

# Copy the application WAR file from the build stage to the autodeploy directory
COPY --from=build /app/target/*.war /opt/payara/glassfish/domains/domain1/autodeploy/

# Copy the AWS secrets setup script and entrypoint script
COPY scripts/setup-aws-secrets.sh /opt/payara/scripts/setup-aws-secrets.sh
COPY scripts/entrypoint.sh /opt/payara/scripts/entrypoint.sh

# Make the scripts executable
RUN chmod +x /opt/payara/scripts/setup-aws-secrets.sh /opt/payara/scripts/entrypoint.sh

# Switch back to payara user (optional but recommended)
USER payara

# Expose the ports for the application and admin dashboard
EXPOSE 8080 4848

# Environment variables for AWS credentials and secrets
ENV AWS_REGION="eu-north-1" \
    DB_SECRET_NAME="" \
    DB_CRED_SECRET_NAME="" \
    ADMIN_SECRET_NAME=""

# Set the entrypoint to our custom script
ENTRYPOINT ["/opt/payara/scripts/entrypoint.sh"]