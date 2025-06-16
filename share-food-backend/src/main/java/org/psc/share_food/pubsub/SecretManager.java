package org.psc.share_food.pubsub;

/**
 * Interface for retrieving secrets from a secure store
 */
public interface SecretManager {

    /**
     * Retrieves a secret value from a secure store
     *
     * @param secretName The name or identifier of the secret
     * @return The secret value as a string, or null if not found
     */
    String getSecret(String secretName);
}
