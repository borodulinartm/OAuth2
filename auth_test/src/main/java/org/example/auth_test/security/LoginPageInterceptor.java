package org.example.auth_test.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

@Component
public class LoginPageInterceptor implements HandlerInterceptor {
    public static final String LOGIN_URL = "/login";
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        // Если мы залогинены, тогда делаем редирект
        if (LOGIN_URL.equals(urlPathHelper.getLookupPathForRequest(request)) && isAuthenticated()) {
            String redirectUrl = response.encodeRedirectURL(
                    request.getContextPath() + "/hello"
            );

            response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            response.setHeader("Location", redirectUrl);

            return false;
        }

        return true;
    }

    // Метод проверяет, аутентифицирован ли пользователь
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && ! (authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.isAuthenticated();
        }

        return false;
    }
}
