package org.psc.share_food.ejb.impl;

import jakarta.ejb.Stateless;
import org.psc.share_food.ejb.api.DonationProcessorRemote;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class DonationProcessorBean implements DonationProcessorRemote {
    
    private static final Logger logger = Logger.getLogger(DonationProcessorBean.class.getName());
    private final Map<Long, String> donationStatuses = new HashMap<>();
    
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

    @Override
    public String getDonationRequestStatus(Long donationRequestId) {
        logger.info("Getting status for donation request with ID: " + donationRequestId);
        if (!donationStatuses.containsKey(donationRequestId)) {
            return "NOT_FOUND";
        }
        return donationStatuses.get(donationRequestId);
    }
}
