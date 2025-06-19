package org.psc.share_food.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.psc.share_food.dao.UserDAO;
import org.psc.share_food.dto.DonationRequestDto;
import org.psc.share_food.entity.DonationRequest;
import org.psc.share_food.entity.User;

import java.util.Optional;

@ApplicationScoped
public class DonationRequestMapper {

    @Inject
    private UserDAO userDAO;

    public DonationRequestDto toDonationRequestDto(DonationRequest donationRequest) {
        if (donationRequest == null) {
            return null;
        }

        return new DonationRequestDto(
                donationRequest.getId(),
                donationRequest.getTitle(),
                donationRequest.getDescription(),
                donationRequest.getOrganization(),
                donationRequest.isDonationTypeFood(),
                donationRequest.isDonationTypeMoney(),
                donationRequest.getPeriodStart(),
                donationRequest.getPeriodEnd(),
                donationRequest.getUser() != null ? donationRequest.getUser().getId() : null,
                donationRequest.getCreatedAt()
        );
    }

    public DonationRequest toDonationRequest(DonationRequestDto dto, User user) {
        if (dto == null) {
            return null;
        }

        DonationRequest donationRequest = new DonationRequest();
        donationRequest.setId(dto.getId());
        donationRequest.setTitle(dto.getTitle());
        donationRequest.setDescription(dto.getDescription());
        donationRequest.setOrganization(dto.getOrganization());
        donationRequest.setDonationTypeFood(dto.isDonationTypeFood());
        donationRequest.setDonationTypeMoney(dto.isDonationTypeMoney());
        donationRequest.setPeriodStart(dto.getPeriodStart());
        donationRequest.setPeriodEnd(dto.getPeriodEnd());
        donationRequest.setUser(user);
        
        if (dto.getCreatedAt() != null) {
            donationRequest.setCreatedAt(dto.getCreatedAt());
        }
        
        return donationRequest;
    }

    public DonationRequest toDonationRequest(DonationRequestDto dto) {
        if (dto == null || dto.getUserId() == null) {
            return null;
        }

        Optional<User> userOptional = userDAO.findById(dto.getUserId());
        if (userOptional.isEmpty()) {
            return null;
        }

        return toDonationRequest(dto, userOptional.get());
    }

    public DonationRequest updateDonationRequestFromDto(DonationRequest entity, DonationRequestDto dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getOrganization() != null) {
            entity.setOrganization(dto.getOrganization());
        }
        entity.setDonationTypeFood(dto.isDonationTypeFood());
        entity.setDonationTypeMoney(dto.isDonationTypeMoney());
        if (dto.getPeriodStart() != null) {
            entity.setPeriodStart(dto.getPeriodStart());
        }
        if (dto.getPeriodEnd() != null) {
            entity.setPeriodEnd(dto.getPeriodEnd());
        }

        return entity;
    }
}