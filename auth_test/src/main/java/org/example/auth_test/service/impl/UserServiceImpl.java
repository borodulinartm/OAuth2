package org.example.auth_test.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.auth_test.models.AuthProvider;
import org.example.auth_test.models.User;
import org.example.auth_test.models.dto.RegisterUserDTO;
import org.example.auth_test.repository.UserRepository;
import org.example.auth_test.service.IUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean registerUser(HttpServletRequest request, RegisterUserDTO registerUserDTO) {
        Optional<User> user = userRepository.findByEmail(registerUserDTO.getEmail());
        if (user.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User curUser = User
                .builder()
                .username(registerUserDTO.getUserName())
                .email(registerUserDTO.getEmail())
                .password(passwordEncoder.encode(registerUserDTO.getPassword()))
                .provider(AuthProvider.APP)
                .build();
        userRepository.save(curUser);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                registerUserDTO.getUserName(), registerUserDTO.getPassword(),
                curUser.getAuthorities()
        );

        Authentication auth = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(auth);
        return true;
    }
}
