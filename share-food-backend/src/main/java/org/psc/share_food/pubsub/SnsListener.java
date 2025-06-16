package org.psc.share_food.pubsub;

/**
 * Interface for listening to SNS events via SQS or other mechanisms
 */
public interface SnsListener {

    /**
     * Starts listening for messages
     */
    void startListening();

    /**
     * Shuts down the listener
     */
    void shutdown();
}
