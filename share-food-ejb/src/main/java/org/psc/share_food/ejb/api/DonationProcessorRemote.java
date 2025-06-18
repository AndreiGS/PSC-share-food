package org.psc.share_food.ejb.api;

import jakarta.ejb.Remote;

/**
 * Remote interface for donation processing
 */
@Remote
public interface DonationProcessorRemote {
    
    /**
     * Process a donation request by its ID
     * @param donationRequestId the ID of the donation request to process
     * @return true if the processing was successful, false otherwise
     */
    boolean processDonationRequest(Long donationRequestId);
    
    /**
     * Get the status of a donation request
     * @param donationRequestId the ID of the donation request
     * @return the status as a string
     */
    String getDonationRequestStatus(Long donationRequestId);
}
