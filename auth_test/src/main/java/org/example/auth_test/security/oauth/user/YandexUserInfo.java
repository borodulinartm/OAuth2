package org.example.auth_test.security.oauth.user;

import org.example.auth_test.models.AuthProvider;

import java.util.Map;

public class YandexUserInfo extends UserInfo {
    public YandexUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getUserID() {
        return (String) attrs.get("id");
    }

    @Override
    public String getUsername() {
        return (String) attrs.get("login");
    }

    @Override
    public String getEmail() {
        return (String) attrs.get("default_email");
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.YANDEX;
    }

    @Override
    public String getNameAttr() {
        return "login";
    }
}
