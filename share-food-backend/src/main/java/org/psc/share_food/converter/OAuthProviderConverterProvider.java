package org.psc.share_food.converter;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import org.psc.share_food.constant.OAuthProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class OAuthProviderConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.equals(OAuthProvider.class)) {
            return new ParamConverter<T>() {
                @Override
                public T fromString(String value) {
                    return rawType.cast(OAuthProvider.byName(value));
                }

                @Override
                public String toString(T value) {
                    return ((OAuthProvider) value).provider();
                }
            };
        }
        return null;
    }
}
