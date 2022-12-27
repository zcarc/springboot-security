package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 이 메서드가 Oauth2 로그인 후에 구글로 받은 userRequest 데이터에 대한 후처리를 진행한다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("userRequest = " + userRequest);
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration()); // "registrationId" 로 어떤 "Oauth"인지 확인 가능
        System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken());
        System.out.println("userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());
        System.out.println("super.loadUser(userRequest) = " + super.loadUser(userRequest));
        System.out.println("super.loadUser(userRequest).getAttributes() = " + super.loadUser(userRequest).getAttributes());

        // username = "google_102921089596693885555"
        // password = "암호화"
        // email = "test@google.com"
        // provider = "google"
        // providerId = "102921089596693885555"

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 회원가입을 강제로 진행할 예정
        return super.loadUser(userRequest);
    }
}
