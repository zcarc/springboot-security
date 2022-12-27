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

- @AuthenticationPrincipal
  - 세션 정보에 접근할 수 있다.
  - 컨트롤러에서 파라미터로 받을 수 있는 타입
    - "UserDetails" 인터페이스를 받아서 이를 구현한 객체로 접근할 수 있다.
    - "UserDetails" 인터페이스를 구현한 객체로 바로 받을 수 있다.


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
    
## public OAuth2User loadUser(OAuth2UserRequest userRequest) 

- 유저 정보가 담긴 userRequest 객체를 받는 과정
  1. 구글 로그인 버튼을 클릭한다.
  2. 구글 로그인 창이 뜬다.
  3. 구글 로그인이 완료된다.
  4. "code"를 리턴 받는다. ("Oauth Client"가 받아준다.)
  5. 받은 "code"로 "AccessToken"을 요청한다.
  6. "AccessToken"를 받는다.

- userRequest 객체로 회원 프로필을 받는 과정 
  - loadUser 메서드를 호출한다.

## authentication.getPrincipal()

- "UserDetails" 인터페이스를 구현한 "PrincipalDetails" 객체가 된다.
- Object 타입의 객체를 리턴하기 때문에 다운캐스팅을 해준다.


## public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails)

- 일반 로그인을 할 때, 해당 컨트롤러의 파라미터에서 두 가지 방법으로 "UserDetails" 타입을 받을 수 있다.

## PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

- 일반 사용자로 로그인한 경우 다운 캐스팅을 "PrincipalDetails"로 해야한다.
- "UserDetails"를 기본적으로 다운캐스팅해야 하는데 이를 구현한 객체인 "PrincipalDetails"는 다형성으로 인해서 다운캐스팅이 가능하다.

## OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal()

- "OAuth"로 로그인을 진행한 경우 다운 캐스팅을 "OAuth2User" 인터페이스로 해야한다.

## oAuth2User.getAttributes()

- "super.loadUser(userRequest).getAttributes()"와 동일한 값이다.

## public String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User)

- OAuth2 로그인을 할 때, 해당 컨트롤러의 파라미터에서 두 가지 방법으로 "OAuth2User" 타입을 받을 수 있다.

## @AuthenticationPrincipal 애노테이션으로 사용자 객체를 파라미터로 받는 경우

- 일반 로그인 또는 OAuth2 로그인으로 하는 경우로 각각 따로 컨트롤러를 생성하는데 한 번에 묶어서 처리하는 방법이 있다.
- PrincipalDetails 클래스가 UserDetails, OAuth2User 인터페이스들을 동시에 구현하면 된다.

## public UserDetails loadUserByUsername(String username), public OAuth2User loadUser(OAuth2UserRequest userRequest)

- 두 메서드는 오버라이딩 하지 않아도 기본적으로 호출이 된다.
- 이걸 오버라이딩 하는 이유는 기본 로그인 또는 OAuth2 로그인 모두를 "PrincipalDetails" 타입으로 리턴해서 "Authentication"에 바인딩 하기 위함이다.
- 서로 다른 로그인 타입에서 기본 타입을 그대로 리턴한다면 각각 오버라이딩 하지 않아도 된다.
- 각각의 메서드가 종료되면 @AuthenticationPrincipal 애노테이션이 생성된다. 그래서 객체에 애노테이션을 사용할 수 있다.