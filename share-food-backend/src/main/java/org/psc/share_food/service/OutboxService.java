package org.psc.share_food.service;

public interface OutboxService {
    void publishEvent(String aggregateType, String aggregateId, String eventType, Object payload);
}
