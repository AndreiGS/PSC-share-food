package org.psc.share_food.ejb.impl;

import jakarta.ejb.Stateless;
import jakarta.ejb.Remote;
import org.psc.share_food.ejb.api.DonationProcessorRemote;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the remote donation processor EJB
 */
@Stateless
@Remote(DonationProcessorRemote.class)
public class DonationProcessorBean implements DonationProcessorRemote {
    
    private static final Logger logger = Logger.getLogger(DonationProcessorBean.class.getName());
    private final Map<Long, String> donationStatuses = new HashMap<>();
    
    /**
     * Process a donation request
     */
    @Override
    public boolean processDonationRequest(Long donationRequestId) {
        logger.info("Processing donation request with ID: " + donationRequestId);
        
        try {
            // Simulate processing
            Thread.sleep(1000);
            
            // Store status
            donationStatuses.put(donationRequestId, "PROCESSED");
            
            logger.info("Successfully processed donation request: " + donationRequestId);
            return true;
        } catch (Exception e) {
            logger.severe("Error processing donation request: " + e.getMessage());
            donationStatuses.put(donationRequestId, "FAILED");
            return false;
        }
    }
    
    /**
     * Get status of a donation request
     */
    @Override
    public String getDonationRequestStatus(Long donationRequestId) {
        logger.info("Getting status for donation request with ID: " + donationRequestId);
        return donationStatuses.getOrDefault(donationRequestId, "UNKNOWN");
    }
}
