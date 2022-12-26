package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Authentication 객체를 만들기 위한 Service 객체

// 시큐리티 설정 (SecurityConfig)에서 loginProcessingUrl("/login") 요청이 오면
// 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 메서드를 호출한다.

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 여기서 String username 변수명은 클라이언트에서 input name="username" 이 아니라면 파라미터에서 값을 가져오지 못한다.
    // 다른 이름으로 설정하려면 SecurityConfig에서 usernameParameter("username2") 이런식으로 바꿔줘야 한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username = " + username);

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            return null;
        }

        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        // 다음과 같이 "Security Session"에서 필요한 타입의 객체이다.
        // Security Session -> Authentication -> UserDetails
        // 여기서 "UserDetails"를 상속한 "PrincipalDetails" 객체를 리턴하면,
        // "Authentication" 객체 내부에 "principalDetails"가 들어가게 된다.
        // 그리고 그 "Authentication" 객체가 "Security Session"으로 들어가게 된다.
        // 이렇게 정상적으로 모두 진행된다면 로그인이 완료된다.
        return principalDetails;
    }

}
