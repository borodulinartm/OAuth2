package org.example.auth_test.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth_test.models.AuthProvider;
import org.example.auth_test.models.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Получаем пользовательский username и password
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // А можно ли так кастить объекты???
        User userDetails = (User) userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            log.error("Username {} not found", username);
            throw new UsernameNotFoundException("Username " + username + " not found");
        } else {
            if (!userDetails.getProvider().equals(AuthProvider.APP)) {
                log.error("You have authenticate through OAuth2");
                throw new RuntimeException("You have authenticate through OAuth2");
            }

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                log.error("Password does not match");
                throw new BadCredentialsException("Bad credentials");
            }
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
