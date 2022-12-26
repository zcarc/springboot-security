package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료되면 시큐리티가 security session을 만들어준다.
// 세션에 시큐리티 만의 세션 공간을 가진다. "Security ContextHolder"에 세션 정보를 저장한다.
// 세션에 들어갈 수 있는 객체는 Authentication 타입 객체로 정해져 있다.
// Authentication 타입 객체 안에는 User 객체가 있어야하는데
// 그 객체는 UserDetails 타입의 객체로 정해져있다.


// 이 클래스는 Authentication 객체에서 사용할 UserDetails 인터페이스를 상속해서 UserDetails 타입의 객체로 만들어준다.

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;

    // 해당 User의 권한을 리턴하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collect = new ArrayList<>();

        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        };

        collect.add(grantedAuthority);

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았나?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠기지 않았나?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드가 만료되지 않았나?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되어있나?
    @Override
    public boolean isEnabled() {

        // 계정을 비활성화 하는 경우
        // ex:
        // 1년 동안 회원이 로그인을 안하면 휴먼 계정으로 전환 된다고 정해져 있다면,
        // User Entity에서 LoginDate를 가져오고
        // 현재 시간 - LoginDate를 해서 1년을 초과하면 false 를 리턴하게 한다.

        return true;
    }
}
