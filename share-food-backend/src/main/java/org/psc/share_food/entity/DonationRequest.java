package org.psc.share_food.entity;

import jakarta.persistence.*;
import org.psc.share_food.constant.OAuthProvider;

import java.time.LocalDate;

@Entity
@Table(name = "donation_requests")
public class DonationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "organization", nullable = false)
    private String organization;

    @Column(name = "donation_type_food")
    private boolean donationTypeFood;

    @Column(name = "donation_type_money")
    private boolean donationTypeMoney;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    public DonationRequest() {
    }

    public DonationRequest(String title, String description, String organization, 
                          boolean donationTypeFood, boolean donationTypeMoney, 
                          LocalDate periodStart, LocalDate periodEnd, User user) {
        this.title = title;
        this.description = description;
        this.organization = organization;
        this.donationTypeFood = donationTypeFood;
        this.donationTypeMoney = donationTypeMoney;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.user = user;
        this.createdAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public DonationRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DonationRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DonationRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOrganization() {
        return organization;
    }

    public DonationRequest setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public boolean isDonationTypeFood() {
        return donationTypeFood;
    }

    public DonationRequest setDonationTypeFood(boolean donationTypeFood) {
        this.donationTypeFood = donationTypeFood;
        return this;
    }

    public boolean isDonationTypeMoney() {
        return donationTypeMoney;
    }

    public DonationRequest setDonationTypeMoney(boolean donationTypeMoney) {
        this.donationTypeMoney = donationTypeMoney;
        return this;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public DonationRequest setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
        return this;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public DonationRequest setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
        return this;
    }

    public User getUser() {
        return user;
    }

    public DonationRequest setUser(User user) {
        this.user = user;
        return this;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public DonationRequest setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}