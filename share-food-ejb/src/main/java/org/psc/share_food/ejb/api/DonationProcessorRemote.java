package org.psc.share_food.ejb.api;

import jakarta.ejb.Remote;

@Remote
public interface DonationProcessorRemote {
    boolean processDonationRequest(Long donationRequestId);
    String getDonationRequestStatus(Long donationRequestId);
}
