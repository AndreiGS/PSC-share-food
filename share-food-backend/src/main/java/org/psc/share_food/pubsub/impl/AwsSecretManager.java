package org.psc.share_food.pubsub.impl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jboss.logging.Logger;
import org.psc.share_food.pubsub.SecretManager;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@ApplicationScoped
public class AwsSecretManager implements SecretManager {

    private static final Logger LOG = Logger.getLogger(AwsSecretManager.class);

    private SecretsManagerClient secretsManagerClient;

    private final Region awsRegion = Region.EU_NORTH_1;

    @Inject
    @Named("awsCredentialsProvider")
    private AwsCredentialsProvider credentialsProvider;

    @PostConstruct
    public void init() {
        this.secretsManagerClient = SecretsManagerClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(awsRegion)
                .build();
    }

    /**
     * Retrieves a secret value from AWS Secrets Manager
     *
     * @param secretName The name or ARN of the secret
     * @return The secret value as a string, or null if not found
     */
    @Override
    public String getSecret(String secretName) {
        try {
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);
            return response.secretString();
        } catch (Exception e) {
            LOG.error("Error retrieving secret: " + secretName, e);
            return null;
        }
    }
}
