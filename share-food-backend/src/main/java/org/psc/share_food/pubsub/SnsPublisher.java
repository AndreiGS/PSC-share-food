package org.psc.share_food.pubsub;

/**
 * Interface for publishing events to SNS or similar message broker
 */
public interface SnsPublisher {

    /**
     * Publishes a user event message
     * 
     * @param message The message to publish
     */
    void publishUserEvent(String message);
}
