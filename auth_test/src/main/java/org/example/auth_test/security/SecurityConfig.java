package org.example.auth_test.security;

import lombok.RequiredArgsConstructor;
import org.example.auth_test.security.oauth.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    private static final String[] AUTH_WHITELIST = {
            "/index",
            "/login"
    };

    @Bean
    public SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(AUTH_WHITELIST).permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer.loginPage("/login");
                    httpSecurityFormLoginConfigurer.usernameParameter("email");
                    httpSecurityFormLoginConfigurer.passwordParameter("pass");
                })
                .oauth2Login(oauth2Login -> {
                    oauth2Login.loginPage("/login");
                    oauth2Login.defaultSuccessUrl("/hello");

                    oauth2Login.userInfoEndpoint(endPoint -> {
                        endPoint.userService(customOAuth2UserService);
                    });
                });
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
