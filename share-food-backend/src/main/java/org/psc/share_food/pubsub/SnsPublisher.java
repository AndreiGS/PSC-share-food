package org.psc.share_food.pubsub;

public interface SnsPublisher {
    void publishUserEvent(String message);
}
