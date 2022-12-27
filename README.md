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


## and().oauth2Login().loginPage("/loginForm")
  - 구글 로그인이 완료된 뒤에 후처리가 필요하다.
  - 구글 로그인이 안료되면 코드를 받지 않고, 액세스 토큰 + 사용자 프로필 정보를 동시에 받는다.
  - 카카오 로그인을 할 경우 
    1. 코드를 받는다. (인증완료)
    2. 액세스 토큰을 받는다.(권한부여가능)
    3. 사용자 프로필 정보를 가져온다.
    4. 가져온 정보를 토대로 가입을 진행한다.
      - 서비스에서 회원가입에 필요한 정보가 충분하다면 회원가입을 자동으로 진행한다.
      - 서비스에서 회원가입에 필요한 정보가 부족하다면 회원가입을 직접 만들어서 진행한다.
    