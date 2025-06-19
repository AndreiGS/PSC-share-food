package org.psc.share_food.dao;

import org.psc.share_food.entity.DonationRequest;
import org.psc.share_food.entity.User;
import org.psc.share_food.utils.repository.GenericDAO;

import java.util.List;

public interface DonationRequestDAO extends GenericDAO<DonationRequest, Long> {
    List<DonationRequest> findByUser(User user);
    List<DonationRequest> findByUserId(Long userId);
}