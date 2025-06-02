package org.psc.share_food.dao;

import org.psc.share_food.entity.DonationRequest;
import org.psc.share_food.entity.User;
import org.psc.share_food.utils.repository.GenericDAO;

import java.util.List;

public interface DonationRequestDAO extends GenericDAO<DonationRequest, Long> {
    /**
     * Find all donation requests created by a specific user
     * @param user The user who created the donation requests
     * @return A list of donation requests created by the user
     */
    List<DonationRequest> findByUser(User user);
    
    /**
     * Find all donation requests created by a user with the given ID
     * @param userId The ID of the user who created the donation requests
     * @return A list of donation requests created by the user
     */
    List<DonationRequest> findByUserId(Long userId);
}