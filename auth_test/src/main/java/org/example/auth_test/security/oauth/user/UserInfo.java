package org.example.auth_test.security.oauth.user;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public abstract class UserInfo {
    protected final Map<String, Object> attrs;

    public abstract String getUserID();
    public abstract String getUsername();
    public abstract String getEmail();
}
