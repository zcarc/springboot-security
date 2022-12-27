package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured 애노테이션 활성화, @PreAuthorize, @PostAuthorize 애노테이션 활성화
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;

//    "UnsatisfiedDependencyException"이 발생해서 "SpringConfig"에 등록했다.
//    Is there an unresolvable circular reference?
//    @Bean
//    public BCryptPasswordEncoder encodePwd() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/user/**").authenticated() // 인증(로그인)만 되면 들어갈 수 있는 주소!
                    .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest()
                    .permitAll()
                .and()
                    .formLogin()
                        .loginPage("/loginForm")
                        // .usernameParameter("username2") // 클라이언트에서 name을 username이 아닌 다른 name으로 설정한 경우 여기서 바꿔줘야 한다.
                        .loginProcessingUrl("/login") // post 요청 /login 이 호출되면 스프링 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                        .defaultSuccessUrl("/") // 직접 "/loginForm" 에서 로그인이 성공하면 "/"로 이동하게 되고, 이외에 요청하는 주소가 권한이 없어서 "/loginForm" 로 이동하게 되었다면 로그인 성공 후 최초에 요청한 주소로 이동하게 된다.
                .and()
                    .oauth2Login()
                        .loginPage("/loginForm")// 구글 로그인이 완료된 뒤에 후처리가 필요하다. Tip. 구글 로그인이 안료되면 코드를 받지 않고, 액세스 토큰 + 사용자 프로필 정보를 동시에 받는다.
                            .userInfoEndpoint()
                                .userService(principalOauth2UserService); // 여기서 Oauth2User 타입의 객체를 전달해야 한다.

        return http.build();
    }

}
