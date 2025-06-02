package org.psc.share_food.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class DonationRequestDto implements Serializable {

    private Long id;
    private String title;
    private String description;
    private String organization;
    private boolean donationTypeFood;
    private boolean donationTypeMoney;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Long userId;
    private LocalDate createdAt;

    public DonationRequestDto() {
    }

    public DonationRequestDto(String title, String description, String organization,
                             boolean donationTypeFood, boolean donationTypeMoney,
                             LocalDate periodStart, LocalDate periodEnd) {
        this.title = title;
        this.description = description;
        this.organization = organization;
        this.donationTypeFood = donationTypeFood;
        this.donationTypeMoney = donationTypeMoney;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public DonationRequestDto(Long id, String title, String description, String organization,
                             boolean donationTypeFood, boolean donationTypeMoney,
                             LocalDate periodStart, LocalDate periodEnd, Long userId, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.organization = organization;
        this.donationTypeFood = donationTypeFood;
        this.donationTypeMoney = donationTypeMoney;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public DonationRequestDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DonationRequestDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DonationRequestDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOrganization() {
        return organization;
    }

    public DonationRequestDto setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public boolean isDonationTypeFood() {
        return donationTypeFood;
    }

    public DonationRequestDto setDonationTypeFood(boolean donationTypeFood) {
        this.donationTypeFood = donationTypeFood;
        return this;
    }

    public boolean isDonationTypeMoney() {
        return donationTypeMoney;
    }

    public DonationRequestDto setDonationTypeMoney(boolean donationTypeMoney) {
        this.donationTypeMoney = donationTypeMoney;
        return this;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public DonationRequestDto setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
        return this;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public DonationRequestDto setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public DonationRequestDto setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public DonationRequestDto setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}