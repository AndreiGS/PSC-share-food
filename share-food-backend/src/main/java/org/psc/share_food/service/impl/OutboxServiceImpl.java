package org.psc.share_food.service.impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import org.psc.share_food.dao.impl.OutboxDAOImpl;
import org.psc.share_food.entity.OutboxEvent;
import org.psc.share_food.service.OutboxService;

@Stateless
public class OutboxServiceImpl implements OutboxService {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    private OutboxDAOImpl outboxDAOImpl;

    public void publishEvent(String aggregateType, String aggregateId, String eventType, Object payload) {
        String jsonPayload = jsonb.toJson(payload);
        OutboxEvent event = new OutboxEvent(aggregateType, aggregateId, eventType, jsonPayload);
        outboxDAOImpl.save(event);
    }
}
