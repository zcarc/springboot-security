## 애노테이션

- @EnableGlobalMethodSecurity
  - 메서드 수준에서 권한을 제어할 수 있게 해준다.
      단순히 허용, 거부 이외에 더 복잡한 규칙을 적용할 수 있다.
  - 속성
    - securedEnabled: @Secured 사용 여부
    - prePostEnabled: @PreAuthorize, @PreFilter ,@PostAuthorize, @PostFilter 사용 여부
    - jsr250Enabled - @RoleAllowed 사용 여부

- @Secured
  - 특정 권한을 가진 사용자만 접근 가능
  - 단순한 권한을 부여할 경우 사용
  - 표현식 사용 불가능

- @PreAuthorize
  - 메서드 호출 전에 인증을 확인
  - 복잡한 권한을 부여할 경우 사용
  - 표현식 사용 가능

- @PostAuthorize
  - 메서드 호출 후에 인증을 확인
  - return 값을 사용해서 인증 확인
  - 복잡한 권한을 부여할 경우 사용
  - 표현식 사용 가능