package org.psc.share_food.service;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import org.psc.share_food.constant.OAuthProvider;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.service.impl.GenericOAuthService;

import java.util.Optional;
import java.util.Set;

public interface OAuthService {
    Optional<UserDto> authenticate(String code, OAuthProvider provider);

    static OAuthService getOAuthServiceByName(OAuthProvider oauthProvider) {
        BeanManager beanManager = CDI.current().getBeanManager();
        String name = oauthProvider.provider() + "-oauth-service";
        Set<Bean<?>> beans = beanManager.getBeans(name);
        if (beans == null || beans.isEmpty()) {
            throw new IllegalArgumentException("No bean found with name: " + name);
        }

        Bean<?> bean = beanManager.resolve(beans);
        CreationalContext<?> ctx = beanManager.createCreationalContext(bean);

        return (OAuthService) beanManager.getReference(bean, OAuthService.class, ctx);
    }
}
