package org.example.auth_test.models;

import lombok.Getter;

// Провайдер аутентификации - ручная аутентификация, google
public enum AuthProvider {
    APP, YANDEX, GOOGLE;

    public String getVal() {
        return name();
    }
}
