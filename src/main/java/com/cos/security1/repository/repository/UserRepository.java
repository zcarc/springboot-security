package com.cos.security1.repository.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository 인터페이스 내부에 @Repository 애노테이션이 등록되어 있어서 JpaRepository를 상속할 경우 따로 등록하지 않아도 된다.
public interface UserRepository extends JpaRepository<User, Integer> {

    // findBy 규칙 -> username 일 경우
    // select * from user where username = 1?
    // 이렇게 "Spring Data JPA"의 네이밍 룰을 이용해서 자동으로 생성해주는 것을 쿼리 메소드라고 한다.
    public User findByUsername(String username);
}
