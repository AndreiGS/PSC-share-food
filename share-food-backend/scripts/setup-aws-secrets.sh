#!/bin/bash
set -e

# Install AWS CLI if not already installed
if ! command -v aws &> /dev/null; then
    echo "Installing AWS CLI..."
    apt-get update
    apt-get install -y curl unzip
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
    unzip awscliv2.zip
    ./aws/install
    rm -rf aws awscliv2.zip
fi

# Check if AWS credentials are available or if IAM role is configured
if [ -z "$AWS_ACCESS_KEY_ID" ] || [ -z "$AWS_SECRET_ACCESS_KEY" ]; then
    echo "AWS credentials not found. Checking for IAM role..."

    # Check if running on EC2 or ECS with IAM role
    TOKEN=$(curl -s -X PUT "http://169.254.169.254/latest/api/token" -H "X-aws-ec2-metadata-token-ttl-seconds: 21600" 2>/dev/null)
    if [ $? -eq 0 ]; then
        ROLE=$(curl -s -H "X-aws-ec2-metadata-token: $TOKEN" http://169.254.169.254/latest/meta-data/iam/security-credentials/ 2>/dev/null)
        if [ -n "$ROLE" ]; then
            echo "IAM role found: $ROLE. Using instance profile for authentication."
        else
            echo "No IAM role found. Make sure AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY environment variables are set or an IAM role is attached to this instance."
            exit 1
        fi
    else
        echo "Not running on EC2/ECS or metadata service is not available. Make sure AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY environment variables are set."
        exit 1
    fi
fi

# Set default region if not provided
if [ -z "$AWS_REGION" ]; then
    AWS_REGION="us-east-1"
    echo "AWS_REGION not set, defaulting to $AWS_REGION"
fi

# Get database credentials from AWS Secrets Manager or use defaults
if [ -z "$DB_SECRET_NAME" ]; then
    echo "DB_SECRET_NAME environment variable not set. Using default database configuration."
    # Default values from domain.xml
    DB_HOST="localhost"
    DB_PORT="3306"
    DB_NAME="share_food"
    DB_USER="root"
    DB_PASSWORD="mysql"
else
    echo "Fetching database credentials from AWS Secrets Manager..."
    # Retrieve the secret value
    SECRET_VALUE=$(aws secretsmanager get-secret-value --secret-id "$DB_SECRET_NAME" --region "$AWS_REGION" --query SecretString --output text)

    # Parse the JSON secret value
    DB_HOST=$(echo "$SECRET_VALUE" | grep -o '"host":"[^"]*"' | cut -d'"' -f4)
    DB_PORT=$(echo "$SECRET_VALUE" | grep -o '"port":"[^"]*"' | cut -d'"' -f4)
    DB_NAME=$(echo "$SECRET_VALUE" | grep -o '"dbname":"[^"]*"' | cut -d'"' -f4)

    SECRET_CRED_VALUE=$(aws secretsmanager get-secret-value --secret-id "$DB_CRED_SECRET_NAME" --region "$AWS_REGION" --query SecretString --output text)
    DB_USER=$(echo "$SECRET_CRED_VALUE" | grep -o '"username":"[^"]*"' | cut -d'"' -f4)
    DB_PASSWORD=$(echo "$SECRET_CRED_VALUE" | grep -o '"password":"[^"]*"' | cut -d'"' -f4)

    # Default values if not found in the secret
    DB_PORT=${DB_PORT:-3306}
    DB_NAME=${DB_NAME:-share_food}

    echo "Database credentials retrieved successfully."
fi

# Configure Payara JDBC connection pool
echo "Configuring Payara JDBC connection pool..."

echo "AS_ADMIN_PASSWORD=admin" > /tmp/passwordfile

# Delete existing connection pool if it exists
#asadmin --user admin --passwordfile /tmp/passwordfile --interactive=false --echo=true delete-jdbc-resource jdbc/mysql || true
#asadmin --user admin --passwordfile /tmp/passwordfile --interactive=false --echo=true delete-jdbc-connection-pool MySQLPool || true

#asadmin add-jdbc-resource --restype javax.sql.DataSource --jndiname jdbc/mysql__pm
#asadmin add-jdbc-connection-pool --datasourceclassname com.mysql.cj.jdbc.MysqlDataSource mysql_pool
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.URL=jdbc:mysql://your_mysql_host:3306/your_database
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.User=your_mysql_user
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.Password=your_mysql_password
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.serverTimezone=UTC # Or your desired timezone
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.useSSL=false # Or true if your MySQL uses SSL
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.requireSSL=false # Or true if your MySQL requires SSL
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.verifyServerCertificate=false # Or true if you want to verify
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.validationTableName=validatePool
#asadmin set server.resources.jdbc-connection-pool.mysql_pool.property.driverClass=com.mysql.cj.jdbc.Driver
#asadmin associate-jdbc-resource --connectionpoolid mysql_pool jdbc/mysql__pm

# Download MySQL Connector and add it to Payara
echo "Downloading MySQL Connector J..."
curl -L -o mysql-connector-j-9.3.0.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.3.0/mysql-connector-j-9.3.0.jar

# Add MySQL Connector to Payara libraries
echo "Adding MySQL Connector to Payara libraries..."
asadmin --user admin --passwordfile /tmp/passwordfile --interactive=false --echo=true add-library mysql-connector-j-9.3.0.jar

# Create new connection pool with AWS credentials
asadmin --user admin --passwordfile /tmp/passwordfile --interactive=false --echo=false create-jdbc-connection-pool \
    --datasourceclassname=com.mysql.cj.jdbc.MysqlDataSource \
    --restype=javax.sql.DataSource \
    --driverclassname=com.mysql.jdbc.Driver \
    --property "URL=jdbc\:mysql\://share-food.c3wmo4a4mjcu.eu-north-1.rds.amazonaws.com\:3306/share_food?createDatabaseIfNotExist\=true\&allowPublicKeyRetrieval\=true\&useSSL\=false\&serverTimezone\=UTC:user=${DB_USER}:password=${DB_PASSWORD}:serverName=share-food.c3wmo4a4mjcu.eu-north-1.rds.amazonaws.com" \
    MySQLPool

echo "Created connection pool"

# Create JDBC resource
asadmin --user admin --passwordfile /tmp/passwordfile --interactive=false --echo=true create-jdbc-resource --connectionpoolid MySQLPool jdbc/mysql

echo "JDBC connection pool configured successfully."

if [ ! -z "$ADMIN_SECRET_NAME" ]; then
    echo "Fetching admin credentials from AWS Secrets Manager..."

    # Retrieve the admin secret value
    ADMIN_SECRET_VALUE=$(aws secretsmanager get-secret-value --secret-id "$ADMIN_SECRET_NAME" --region "$AWS_REGION" --query SecretString --output text)

    # Parse the JSON secret value
    ADMIN_PASSWORD=$(echo "$ADMIN_SECRET_VALUE" | grep -o '"password":"[^"]*"' | cut -d'"' -f4)

    echo "AS_ADMIN_PASSWORD=" > /tmp/passwordfile
    echo "AS_ADMIN_NEWPASSWORD=${ADMIN_PASSWORD}" >> /tmp/passwordfile

    echo "Configuring admin credentials..."
    asadmin --user admin --passwordfile /tmp/passwordfile --interactive=false --echo=true change-admin-password

    rm -rf /tmp/passwordfile
    echo "AS_ADMIN_PASSWORD=${ADMIN_PASSWORD}" > /tmp/passwordfile
    echo "Admin credentials configured successfully."
fi

echo "AWS configuration completed successfully."
