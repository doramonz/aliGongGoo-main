package com.doramonz.aligonggoo.config.security;

import com.doramonz.aligonggoo.domain.User;
import com.doramonz.aligonggoo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            String id = "kakao_" + oAuth2User.getAttribute("id");
            if(!userService.existUser(id)){
                User user = userService.createUser(id);
                oAuth2User = new CustomUser(user.getId());
            }else{
                oAuth2User = new CustomUser(id);
            }
            userService.updateLastLogin(id);
        }

        return oAuth2User;
    }
}
