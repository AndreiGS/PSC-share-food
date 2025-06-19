package org.psc.share_food.pubsub;

public interface SnsListener {

    void startListening();
    void shutdown();
}
