package org.psc.share_food.service;

import org.psc.share_food.dto.DonationRequestDto;
import org.psc.share_food.security.UserDetail;

import java.util.List;
import java.util.Optional;

public interface DonationRequestService {
    DonationRequestDto createDonationRequest(DonationRequestDto donationRequestDto, UserDetail userDetail);
    Optional<DonationRequestDto> getDonationRequest(Long id);
    List<DonationRequestDto> getDonationRequestsForUser(UserDetail userDetail);
    List<DonationRequestDto> getDonationRequestsForUser(Long userId);
}