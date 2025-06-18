package org.psc.share_food.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.naming.Context;
import jakarta.naming.InitialContext;
import jakarta.naming.NamingException;
import org.psc.share_food.ejb.api.DonationProcessorRemote;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client helper for accessing the remote DonationProcessor EJB
 */
@Singleton
@Startup
public class DonationProcessorRemoteClient {
    
    private static final Logger logger = Logger.getLogger(DonationProcessorRemoteClient.class.getName());
    
    private DonationProcessorRemote donationProcessor;
    
    @PostConstruct
    public void init() {
        try {
            lookupRemoteEJB();
        } catch (NamingException e) {
            logger.log(Level.SEVERE, "Failed to initialize remote EJB client", e);
        }
    }
    
    private void lookupRemoteEJB() throws NamingException {
        // Get configuration from system properties with defaults
        String ejbHost = System.getProperty("ejb.host", "localhost");
        String ejbPort = System.getProperty("ejb.port", "8081");
        String ejbBaseUrl = System.getProperty("ejb.base.url", "http://" + ejbHost + ":" + ejbPort);
        
        logger.info("Connecting to remote EJB at: " + ejbBaseUrl);
        
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, ejbBaseUrl);
        props.put(Context.SECURITY_PRINCIPAL, "admin");
        props.put(Context.SECURITY_CREDENTIALS, "admin123");
        
        Context context = new InitialContext(props);
        
        donationProcessor = (DonationProcessorRemote) context.lookup(
                "ejb:/share-food-ejb/DonationProcessorBean!org.psc.share_food.ejb.api.DonationProcessorRemote");
        
        logger.info("Successfully connected to remote EJB");
    }
    
    /**
     * Process a donation request using the remote EJB
     * @param donationRequestId ID of the donation request
     * @return true if processing was successful, false otherwise
     */
    public boolean processDonationRequest(Long donationRequestId) {
        try {
            if (donationProcessor == null) {
                lookupRemoteEJB();
            }
            return donationProcessor.processDonationRequest(donationRequestId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing donation request: " + donationRequestId, e);
            return false;
        }
    }
    
    /**
     * Get the status of a donation request from the remote EJB
     * @param donationRequestId ID of the donation request
     * @return status of the donation request
     */
    public String getDonationRequestStatus(Long donationRequestId) {
        try {
            if (donationProcessor == null) {
                lookupRemoteEJB();
            }
            return donationProcessor.getDonationRequestStatus(donationRequestId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting donation request status: " + donationRequestId, e);
            return "ERROR";
        }
    }
}
