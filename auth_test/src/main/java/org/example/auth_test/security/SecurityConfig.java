package org.example.auth_test.security;

import lombok.RequiredArgsConstructor;
import org.example.auth_test.security.oauth.CustomOAuth2UserService;
import org.example.auth_test.security.oauth.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final MyAuthenticationProvider myAuthenticationProvider;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private static final String[] AUTH_WHITELIST = {
        "/index", "/login"
    };

    @Bean
    public SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(AUTH_WHITELIST).permitAll()
                            .requestMatchers("/register").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer.loginPage("/login");
                    httpSecurityFormLoginConfigurer.usernameParameter("email");
                    httpSecurityFormLoginConfigurer.passwordParameter("pass");
                })
                .oauth2Login(oauth2Login -> {
                    oauth2Login.loginPage("/login");

                    oauth2Login.userInfoEndpoint(endPoint -> {
                        endPoint.userService(customOAuth2UserService);
                    });

                    oauth2Login.successHandler(oAuth2SuccessHandler);
                })
                .authenticationProvider(myAuthenticationProvider)
                .logout(config -> {
                    config.logoutUrl("/logout");
                    config.logoutSuccessUrl("/login");
                    config.clearAuthentication(true).deleteCookies().invalidateHttpSession(true);
                });
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        MyAuthenticationProvider provider = new MyAuthenticationProvider(userDetailsService, passwordEncoder);
        return provider::authenticate;
    }
}
