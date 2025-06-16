#!/bin/bash
set -e

echo "Starting Payara server in background..."
asadmin start-domain

# Wait for HTTP port (4848)
echo "Waiting for Payara HTTP port 4848..."
until curl --output /dev/null --silent --head --fail http://localhost:4848; do
    printf '.'
    sleep 2
done
echo "HTTP port 4848 is available."

# Run AWS secrets setup if present
if [ -f "/opt/payara/scripts/setup-aws-secrets.sh" ]; then
    echo "Running AWS secrets setup script..."
    bash "/opt/payara/scripts/setup-aws-secrets.sh"
fi

export ADMIN_PASSWORD_FILE=/tmp/passwordfile

# Deploy WAR files
AUTODEPLOY_DIR="/opt/payara/glassfish/domains/domain1/autodeploy"
for war_file in "$AUTODEPLOY_DIR"/*.war; do
    [ -e "$war_file" ] || continue
    echo "Deploying $war_file..."
    asadmin --user admin --passwordfile "$ADMIN_PASSWORD_FILE" deploy "$war_file"
done

# Stop background Payara process before starting in foreground
echo "Stopping background Payara domain..."
asadmin stop-domain

echo "Starting Payara server in foreground..."
exec asadmin start-domain --verbose
