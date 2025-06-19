package org.psc.share_food.ejb;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import org.psc.share_food.ejb.api.DonationProcessorRemote;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DonationProcessorRemoteClient {

    private static final Logger logger = Logger.getLogger(DonationProcessorRemoteClient.class.getName());

    @EJB(lookup = "java:global/share-food-ejb/DonationProcessorBean!org.psc.share_food.ejb.api.DonationProcessorRemote")
    private DonationProcessorRemote donationProcessor;

    public boolean processDonationRequest(Long donationRequestId) {
        try {
            return donationProcessor.processDonationRequest(donationRequestId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing donation request: " + donationRequestId, e);
            return false;
        }
    }

    public String getDonationRequestStatus(Long donationRequestId) {
        try {
            return donationProcessor.getDonationRequestStatus(donationRequestId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing donation request: " + donationRequestId, e);
            return "ERROR";
        }
    }
}
