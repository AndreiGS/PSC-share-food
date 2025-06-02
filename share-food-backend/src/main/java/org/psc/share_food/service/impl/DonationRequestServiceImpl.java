package org.psc.share_food.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.psc.share_food.dao.DonationRequestDAO;
import org.psc.share_food.dao.UserDAO;
import org.psc.share_food.dto.DonationRequestDto;
import org.psc.share_food.entity.DonationRequest;
import org.psc.share_food.entity.User;
import org.psc.share_food.mapper.DonationRequestMapper;
import org.psc.share_food.security.UserDetail;
import org.psc.share_food.service.DonationRequestService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class DonationRequestServiceImpl implements DonationRequestService {

    @Inject
    private DonationRequestDAO donationRequestDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private DonationRequestMapper donationRequestMapper;

    @Inject
    public DonationRequestServiceImpl(DonationRequestDAO donationRequestDAO, UserDAO userDAO, DonationRequestMapper donationRequestMapper) {
        this.donationRequestDAO = donationRequestDAO;
        this.userDAO = userDAO;
        this.donationRequestMapper = donationRequestMapper;
    }

    @Override
    public DonationRequestDto createDonationRequest(DonationRequestDto donationRequestDto, UserDetail userDetail) {
        Optional<User> userOptional = userDAO.findById(userDetail.getId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        DonationRequest donationRequest = donationRequestMapper.toDonationRequest(donationRequestDto, user);
        donationRequest.setCreatedAt(LocalDate.now());
        
        DonationRequest savedDonationRequest = donationRequestDAO.save(donationRequest);
        return donationRequestMapper.toDonationRequestDto(savedDonationRequest);
    }

    @Override
    public Optional<DonationRequestDto> getDonationRequest(Long id) {
        return donationRequestDAO.findById(id)
                .map(donationRequest -> donationRequestMapper.toDonationRequestDto(donationRequest));
    }

    @Override
    public List<DonationRequestDto> getDonationRequestsForUser(UserDetail userDetail) {
        return getDonationRequestsForUser(userDetail.getId());
    }

    @Override
    public List<DonationRequestDto> getDonationRequestsForUser(Long userId) {
        List<DonationRequest> donationRequests = donationRequestDAO.findByUserId(userId);
        return donationRequests.stream()
                .map(donationRequest -> donationRequestMapper.toDonationRequestDto(donationRequest))
                .collect(Collectors.toList());
    }
}