package org.example.auth_test.security.oauth;

import lombok.RequiredArgsConstructor;
import org.example.auth_test.models.AuthProvider;
import org.example.auth_test.models.User;
import org.example.auth_test.repository.UserRepository;
import org.example.auth_test.security.oauth.user.GoogleUserInfo;
import org.example.auth_test.security.oauth.user.UserInfo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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

        // TODO: переписать блок кода: могут быть другие провайдеры
        UserInfo userInfo = new GoogleUserInfo(oAuth2User.getAttributes());

        User curUser = userRepository.findByUsername(userInfo.getEmail());
        if (curUser == null) {
            curUser = User
                    .builder()
                    .provider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()))
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

        return new CustomOAuth2User(clientName, oAuth2User);
    }
}
