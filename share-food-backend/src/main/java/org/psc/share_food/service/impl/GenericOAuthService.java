package org.psc.share_food.service.impl;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.psc.share_food.constant.OAuthProvider;
import org.psc.share_food.dto.UserAuthenticatedEvent;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.service.OAuthService;
import org.psc.share_food.service.OutboxService;

import java.util.Optional;

public abstract class GenericOAuthService implements OAuthService {
    private static final Logger LOG = Logger.getLogger(GenericOAuthService.class);

    @Inject
    private OutboxService outboxServiceImpl;

    abstract Optional<UserDto> authenticateByCode(String code);

    @Transactional
    public Optional<UserDto> authenticate(String code, OAuthProvider provider) {
        Optional<UserDto> userOptional = authenticateByCode(code);

        // Implement outbox pattern for user authentication events
        userOptional.ifPresent(user -> {
            try {
                // Create the event and publish to outbox
                UserAuthenticatedEvent event = new UserAuthenticatedEvent(user, provider.provider());
                outboxServiceImpl.publishEvent(
                    "user",      // aggregate type
                    user.getId().toString(),  // aggregate id
                    "user.authenticated",     // event type
                    event                     // full user event payload
                );

                LOG.info("Published user authenticated event to outbox for user: " + user.getUsername());
            } catch (Exception e) {
                // Log the error but don't fail the authentication - this is a non-critical operation
                LOG.error("Failed to publish user authenticated event to outbox", e);
            }
        });

        return userOptional;
    }
}
