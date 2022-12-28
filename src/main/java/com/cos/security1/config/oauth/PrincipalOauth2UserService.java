package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.FacebookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.NaverUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    // 이 메서드가 Oauth2 로그인 후에 구글로 받은 userRequest 데이터에 대한 후처리를 진행한다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("userRequest = " + userRequest);
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration()); // "registrationId" 로 어떤 "Oauth"인지 확인 가능
        System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken());
        System.out.println("userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());

        // username = "google_102921089596693885555"
        // password = "암호화"
        // email = "test@google.com"
        // provider = "google"
        // providerId = "102921089596693885555"

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("super.loadUser(userRequest) = " + super.loadUser(userRequest));
        System.out.println("OAuth2User oAuth2User = super.loadUser(userRequest);");
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());

        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());

        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));

        } else {
            System.out.println("구글, 페이스북, 네이버 사용자가 아닙니다.");
            return null;
        }

        // 회원가입을 강제로 진행할 예정
//        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
//        String providerId = oAuth2User.getAttribute("sub"); // google: sub, facebook: id
//        String username = provider + "_" + providerId; // google_102921089596693885555
//        String password = bCryptPasswordEncoder.encode("HyunsooLee"); // 여기서 패스워드는 의미는 없지만 배우는 과정이니 생성해준다.
//        String email = oAuth2User.getAttribute("email");
//        String role = "ROLE_USER";

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("HyunsooLee");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        // 기존에 유저가 회원가입이 되어있는지 검증
        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            System.out.println("OAuth2 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(userEntity);
        } else {
            System.out.println("OAuth2 로그인을 이미 한적이 있습니다. 당신은 자동 회원가입이 이미 되어 있습니다.");
        }

        PrincipalDetails principalDetails = new PrincipalDetails(userEntity, oAuth2User.getAttributes());

        // Authentication 객체에 바인딩 된다.
        return principalDetails;
    }
}
