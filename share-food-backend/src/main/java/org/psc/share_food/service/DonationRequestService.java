package org.psc.share_food.service;

import org.psc.share_food.dto.DonationRequestDto;
import org.psc.share_food.security.UserDetail;

import java.util.List;
import java.util.Optional;

public interface DonationRequestService {
    /**
     * Create a new donation request for the authenticated user
     * @param donationRequestDto The donation request data
     * @param userDetail The authenticated user
     * @return The created donation request
     */
    DonationRequestDto createDonationRequest(DonationRequestDto donationRequestDto, UserDetail userDetail);
    
    /**
     * Get a donation request by ID
     * @param id The ID of the donation request
     * @return The donation request, if found
     */
    Optional<DonationRequestDto> getDonationRequest(Long id);
    
    /**
     * Get all donation requests for the authenticated user
     * @param userDetail The authenticated user
     * @return A list of donation requests for the user
     */
    List<DonationRequestDto> getDonationRequestsForUser(UserDetail userDetail);
    
    /**
     * Get all donation requests for a user with the given ID
     * @param userId The ID of the user
     * @return A list of donation requests for the user
     */
    List<DonationRequestDto> getDonationRequestsForUser(Long userId);
}