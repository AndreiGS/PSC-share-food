package org.psc.share_food.dao;

import org.psc.share_food.entity.OutboxEvent;
import org.psc.share_food.utils.repository.GenericDAO;

import java.util.UUID;

public interface OutboxDAO extends GenericDAO<OutboxEvent, UUID> {
}
