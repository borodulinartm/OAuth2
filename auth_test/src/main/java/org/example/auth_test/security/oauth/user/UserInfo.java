package org.example.auth_test.security.oauth.user;

import lombok.RequiredArgsConstructor;
import org.example.auth_test.models.AuthProvider;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@RequiredArgsConstructor
public abstract class UserInfo {
    protected final Map<String, Object> attrs;

    // Фабричный метод для создания нового объекта
    public static UserInfo createUserInfo(String clientName, OAuth2User oAuth2User) {
        if (clientName.equalsIgnoreCase(AuthProvider.GOOGLE.getVal()))
            return new GoogleUserInfo(oAuth2User.getAttributes());
        else if (clientName.equalsIgnoreCase(AuthProvider.YANDEX.getVal()))
            return new YandexUserInfo(oAuth2User.getAttributes());
        else
            throw new OAuth2AuthorizationException(
                    new OAuth2Error("1", "No provider detected", ""));
    }

    public abstract String getUserID();
    public abstract String getUsername();
    public abstract String getEmail();

    // Extra methods
    public abstract AuthProvider getProvider();
    public abstract String getNameAttr();
}
