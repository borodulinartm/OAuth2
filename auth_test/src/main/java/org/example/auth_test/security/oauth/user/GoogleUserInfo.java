package org.example.auth_test.security.oauth.user;

import org.example.auth_test.models.AuthProvider;

import java.util.Map;

public class GoogleUserInfo extends UserInfo {
    public GoogleUserInfo(Map<String, Object> elems) {
        super(elems);
    }

    @Override
    public String getUserID() {
        return (String) attrs.get("sub");
    }

    @Override
    public String getUsername() {
        return (String) attrs.get("email");
    }

    @Override
    public String getEmail() {
        return (String) attrs.get("email");
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.GOOGLE;
    }

    @Override
    public String getNameAttr() {
        return "email";
    }
}
