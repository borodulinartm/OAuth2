package org.example.auth_test.security.oauth;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.example.auth_test.models.AuthProvider;
import org.example.auth_test.models.User;
import org.example.auth_test.repository.UserRepository;
import org.example.auth_test.security.oauth.user.GoogleUserInfo;
import org.example.auth_test.security.oauth.user.UserInfo;
import org.example.auth_test.security.oauth.user.YandexUserInfo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        UserInfo userInfo = UserInfo.createUserInfo(clientName, oAuth2User);
        createOrUpdateUser(userInfo);

        return new CustomOAuth2User(clientName, oAuth2User, userInfo.getNameAttr());
    }

    private void createOrUpdateUser(@Nonnull UserInfo userInfo) {
        User curUser = userRepository.findByUsername(userInfo.getEmail());
        if (curUser == null) {
            curUser = User
                    .builder()
                    .provider(userInfo.getProvider())
                    .providerID(userInfo.getUserID())
                    .name(userInfo.getUsername())
                    .username(userInfo.getUsername())
                    .email(userInfo.getEmail())
                    .build();
            userRepository.save(curUser);
        } else {
            curUser.setName(userInfo.getUsername());
            userRepository.save(curUser);
        }
    }
}
