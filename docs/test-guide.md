# Test Guide

* 안전하게 테스트 짜는 방법
* 통일성 있는 테스트 코드

# 테스트 정책

| 어노테이션           | 설명                   | 부모 클래스          | Bean         |
| --------------- | -------------------- | --------------- | ------------ |
| @SpringBootTest | 통합 테스트, 전체           | IntegrationTest | Bean 전체      |
| @WebMvcTest     | 단위 테스트, Mvc 테스트      | MockApiTest     | MVC 관련된 Bean |
| @DataJpaTest    | 단위 테스트, Jpa 테스트      | RepositoryTest  | JPA 관련 Bean  |
| @RestClientTest | 단위 테스트, Rest API 테스트 | 일부 Bean         | MockBean     |
| None            | 단위 테스트, Service 테스트  | MockTest        | None         |
| None            | POJO, 도메인 테스트        | 일부 Bean         | None         |


# 테스트 코드가 주는 장점
* 냄세나느 코드를 빨리 알 수 있다.
* 테스트 작성이 어렵다는 것은 로직의 문제가 있다는 것이다.


# 테스트 코드 작성시 주의
* rollback 되어야함
* 테스트의 순서와 상관이 수행
* 데이터베이스에 의존하지 않은 코드 작성


# 통합테스트

## 장점
* 모든 Bean을 올리고 테스트를 진행하기 때문에 쉽게 테스트 진행 가능
* 모든 Bean을 올리고 테스트를 진행하기 때문에 운영환경과 가장 유사하게 테스트 가능
* API를 테스트할 경우 요청부터 응답까지 전체적인 테스트 진행 가능

## 단점
* 모든 Bean을 올리고 테스트를 진행하기 때문에 테스트 시간이 오래 걸림
* 테스트의 단위가 크기 때문에 테스트 실패시 디버깅이 어려움
* 외부 API 콜같은 Rollback 처리가 안되는 테스트 진행을 하기 어려움

## Code

### IntegrationTest
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApp.class)
@AutoConfigureMockMvc
@ActiveProfiles(TestProfile.TEST)
@Transactional
@Ignore
public class IntegrationTest {
    @Autowired protected MockMvc mvc;
    @Autowired protected ObjectMapper objectMapper;
    ...
}
```
* 통합 테스트의 Base 클래스입니다. Base 클래스를 통해서 테스트 전략을 통일성 있게 가져갈 수 있습니다.
* 통합 테스트는 주로 컨트롤러 테스트를 주로 하며 요청부터 응답까지의 전체 플로우를 테스트합니다.
* `@ActiveProfiles(TestProfile.TEST)` 설정으로 테스트에 profile을 지정합니다. 환경별로 properties 파일을 관리하듯이 test도 반드시 별도의 properties 파일로 관리하는 것이 바람직합니다.
* 인터페이스나 enum 클래스를 통해서 profile을 관리합니다. 오타 실수를 줄일 수 있으며 전체적인 프로필이 몇 개 있는지 한 번에 확인할 수 있습니다.
* `@Transactional` 트랜잭션 어노테이션을 추가하면 테스트코드의 데이터베이스 정보가 자동으로 Rollback 됩니다. 베이스 클래스에 이 속성을 추가 해야지 실수 없이 진행할 수 있습니다.
* `@Transactional`을 추가하면 자연스럽게 데이터베이스 상태의존 적인 테스트를 자연스럽게 하지 않을 수 있게 됩니다.
* 통합 테스트 시 필요한 기능들을 `protected`로 제공해줄 수 있습니다. API 테스트를 주로 하게 되니 ObjectMapper 등을 제공해줄 수 있습니다. 유틸성 메서드들도 `protected`로 제공해주면 중복 코드 및 테스트 코드의 편의성이 높아집니다.
* 실제로 동작할 필요가 없으니 `@Ignore` 어노테이션을 추가합니다.

### Test Code
```java
public class MemberApiTest extends IntegrationTest {

    @Autowired
    private MemberSetup memberSetup;

    @Test
    public void 회원가입_성공() throws Exception {
        //given
        final Member member = MemberBuilder.build();
        final Email email = member.getEmail();
        final Name name = member.getName();
        final SignUpRequest dto = SignUpRequestBuilder.build(email, name);

        //when
        final ResultActions resultActions = requestSignUp(dto);

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("email.value").value(email.getValue()))
                .andExpect(jsonPath("email.host").value(email.getHost()))
                .andExpect(jsonPath("email.id").value(email.getId()))
                .andExpect(jsonPath("name.first").value(name.getFirst()))
                .andExpect(jsonPath("name.middle").value(name.getMiddle()))
                .andExpect(jsonPath("name.last").value(name.getLast()))
                .andExpect(jsonPath("name.fullName").value(name.getFullName()))
        ;
    }

    @Test
    public void 회원조회() throws Exception {
        //given
        final Member member = memberSetup.save();

        //when
        final ResultActions resultActions = requestGetMember(member.getId());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("email.value").value(member.getEmail().getValue()))
                .andExpect(jsonPath("email.host").value(member.getEmail().getHost()))
                .andExpect(jsonPath("email.id").value(member.getEmail().getId()))
                .andExpect(jsonPath("name.first").value(member.getName().getFirst()))
                .andExpect(jsonPath("name.middle").value(member.getName().getMiddle()))
                .andExpect(jsonPath("name.last").value(member.getName().getLast()))
                .andExpect(jsonPath("name.fullName").value(member.getName().getFullName()))
        ;
    }

    private ResultActions requestSignUp(SignUpRequest dto) throws Exception {
        return mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }
    ...
}
```
* `IntegrationTest` 클래스를 상속받습니다. 이 상속을 통해서 MemberApiTest에서 테스트를 위한 어노테이션이 생략되며 어떤 통합 테스트라도 항상 통일성을 갖을 수 있습니다.
* `given`, `when`, `then` 키워드로 테스트 흐름을 알려줍니다. 다른 사람의 테스트 코드의 가독성이 높아지기 때문에 해당 키워드로 적절하게 표시하는것을 권장합니다.
* 요청에 대한 메서드를 `requestSignUp(...)` 으로 분리해서 재사용성을 높입니다. 해당 메서드로 valdate 실패하는 케이스도 작성합니다 `andDo(print())` 메서드를 추가해서 해당 요청에 대한 출력을 확인합니다. 디버깅에 매우 유용합니다.
* 모든 response에 대한 `andExpect`를 작성합니다. 간혹 `.andExpect(content().string(containsString("")))` 이런 테스트를 진행하는데 특정 문자열이 들어 있는지 없는지 확인하는 것은 것보다 모든 Reponse에 대한 검증을 지향합니다.
  * **response에 하나라도 빠지거나 변경되면 API 변경이 이루어진 것이고 그 변경에 맞게 테스트 코드도 변경되어야 합니다.**
* `회원 조회` 테스트 강은 경우 `memberSetup.save();` 메서드로 테스트전에 데이터베이스에 insert 합니다. 
  * 데이터베이스에 미리 있는 값을 검증하는 것은 데이터베이스 상태에 의존한 코드가 되며 누군가가 회원 정보를 변경하게 되면 테스트 코드가 실패하게 됩니다.
  * 테스트 전에 데이터를 insert하지 않는다면 테스트 코드 구동 전에 `.sql` 으로 미리 데이터베이스를 준비시킵니다 ApplicationRunner를 이용해서 데이터베이스를 준비시키 방법도 있습니다.
  * **중요한 것은 데이터베이스 상태에 너무 의존적인 테스트는 향후 로직의 문제가 없더라도 테스트가 실패하는 상황이 자주 만나게 됩니다.**

# 서비스 테스트

## 장점
* 진행하고자 하는 테스트에만 집중할 수 있습니다.
* 테스트 진행시 중요 관점이 아닌 것들은 Mocking 처리해서 외부 의존성들을 줄일 수 있습니다.
  * 예를들어 주문 할인 로직이 제대로 동작하는지에 대한 테스트만 진행하지 이게 실제로 데이터베이스에 insert되는지 여부는 해당 테스트의 관심사가 아닙니다.
* 테스트 속도가 빠릅니다.

## 단점
* 의존성있는 객체를 Mocking 하기 때문에 문제가 완결된 테스트는 아닙니다.
* Mocking 하기가 귀찮습니다.
* Mocking 라이브러리에 대한 학습 비용이 발생합니다.

## Code

### MockTest
```java
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(TestProfile.TEST)
@Ignore
public class MockTest {

}
```
* 주로 Service 영역을 테스트 합니다. 
* `MockitoJUnitRunner`을 통해서 Mock 테스트를 진행합니다.

### Test Code
```java
public class MemberSignUpServiceTest extends MockTest {

    @InjectMocks
    private MemberSignUpService memberSignUpService;

    @Mock
    private MemberRepository memberRepository;
    private Member member;

    @Before
    public void setUp() throws Exception {
        member = MemberBuilder.build();
    }

    @Test
    public void 회원가입_성공() {
        //given
        final Email email = member.getEmail();
        final Name name = member.getName();
        final SignUpRequest dto = SignUpRequestBuilder.build(email, name);

        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.save(any())).willReturn(member);

        //when
        final Member signUpMember = memberSignUpService.doSignUp(dto);

        //then
        assertThat(signUpMember).isNotNull();
        assertThat(signUpMember.getEmail().getValue()).isEqualTo(member.getEmail().getValue());
        assertThat(signUpMember.getName().getFullName()).isEqualTo(member.getName().getFullName());
    }

    @Test(expected = EmailDuplicateException.class)
    public void 회원가입_이메일중복_경우() {
        //given
        final Email email = member.getEmail();
        final Name name = member.getName();
        final SignUpRequest dto = SignUpRequestBuilder.build(email, name);

        given(memberRepository.existsByEmail(any())).willReturn(true);

        //when
        memberSignUpService.doSignUp(dto);
    }
}
```
* `MockTest` 객체를 상속받아 테스트의 일관성을 갖습니다.
* `회원가입_성공` 테스트는 오직 회원 가입에대한 단위 테스트만 진행합니다.
  * `existsByEmail`을 모킹해서 해당 이메일이 중복되지 않았다는 가정을 합니다.
  * `then` 에서는 회원 객체가 해당 비지니스 요구사항에 맞게 생성됬는지를 검사합니다.
  * 실제 데이터베이스에 Insert 됬는지 여부는 해당 테스트의 관심사가 아닙니다.
* `회원가입_이메일중복_경우` 테스트는 회원가입시 이메일이 중복됬는지 여부를 확인합니다.
  * `existsByEmail`을 모킹해서 이메일이 중복됬다는 가정을 합니다.
  * `expected`으로 이메일이 중복되었을 경우 `EmailDuplicateException` 예외가 발생하는지 확인합니다.
  * 해당 이메일이 데이터베이스에 실제로 있어서 예외가 발생하는지는 관심사가 아닙니다. 작성한 코드가 제대로 동작 여부만이 해당 테스트의 관심사 입니다.
* 오직 테스트의 관심사만 테스트를 진행하기 때문에 예외 발생시 디버깅 작업도 명확해집니다.
* 외부 의존도가 낮기 때문에 테스트 하고자하는 부분만 명확하게 테스트가 가능합니다.
  * 이것은 단점이기도 합니다. 해당 테스트만 진행하지 외부 의존을 갖는 코드까지 테스트하지 않으니 실제 환경에서 제대로 동작하지 않을 가능성이 있습니다. 외부 의존에대한 테스트는 통합 테스트에서 진행합니다.


# Mock API 테스트

## 장점
* Mock 테스트와 장점은 거의 동일합니다.
* `WebApplication` 관련된 Bean들만 등록하기 때문에 통합 테스트 보다 빠르게 테스트 할 수 있습니다.
* 통합 테스트를 진행하기 어려운 테스트를 진행합니다.
  * 외부 API 같은 Rollback 처리가 힘들거나 불가능한 테스트를 주로 사용합니다.
  * 예를 들어 외부 결제 모듈 API를 콜하면 안돼는 케이스에서 주로 사용 할 수 있습니다.
  * 이런 문제는 통합 테스트에서 해당 객체를 Mock 객체로 변경해서 테스트를 변경해서 테스트를 할 수도 있습니다.

## 단점
* Mcok 테스트와 다점은 거의 동일합니다.
* 요청 부터 응답까지 모든 테스트를 Mock 기반으로 테스트하기 때문에 실제 환경에서는 제대로 동작하지 않을 가능성이 매우큽니다.

## Code
```java
@WebMvcTest(MemberApi.class)
public class MemberMockApiTest extends MockApiTest {
    @MockBean private MemberSignUpService memberSignUpService;
    @MockBean private MemberHelperService memberHelperService;
    ...

    @Test
    public void 회원가입_유효하지않은_입력값() throws Exception {
        //given
        final Email email = Email.of("asdasd@d"); // 이메일 형식이 유효하지 않음
        final Name name = Name.builder().build();
        final SignUpRequest dto = SignUpRequestBuilder.build(email, name);
        final Member member = MemberBuilder.build();

        given(memberSignUpService.doSignUp(any())).willReturn(member);

        //when
        final ResultActions resultActions = requestSignUp(dto);

        //then
        resultActions
                .andExpect(status().isBadRequest())
        ;

    }
```
* `@WebMvcTest(MemberApi.class)` 어노테이션을 통해서 하고자하는 `MemberApi`의 테스트를 진행합니다.
* `@MockBean` 으로 객체를 주입받아 Mocking 작업을 진행합니다.
* 테스트의 관심사는 오직 Request와 그에 따른 Response 입니다. 

# Repository Test

## 장점
* `Repository` 관련된 Bean들만 등록하기 때문에 통합 테스트에 비해서 빠릅니다.
* `Repository`에 대한 관심사만 갖기 때문에 테스트 범위가 작습니다.

## 단점
* 테스트 범위가 작기 때문에 실제 환경과 차이가 발생합니다. 

## Code

### RepositoryTest
```java
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(TestProfile.TEST)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Ignore
public class RepositoryTest {
}
```
* `@DataJpaTest` 어노테이션을 통해서 `Repository`에 대한 Bean만 등록합니다.
* `@DataJpaTest`는 기본적으로 메모리 데이터베이스에 대한 테스트를 진행합니다. `@AutoConfigureTestDatabase` 어노테이션을 통해서 profile에 등록된 데이터베이스 정보로 대체할 수 있습니다.
* `JpaRepository`에서 기본적으로 기본적으로 재공해주는 `findById`, `findByAll`, `deleteById`등은 테스트를 하지 않습니다.
  * 기본적으로 `save()` null 제약 조건등의 테스트는 진행해도 좋다고 생각합니다.
  * 주로 커스텀하게 작성한 쿼리 메서드, `@Query`으로 작성된 JPQL등의 커스텀하게 추가된 메서드를 테스트합니다.

### Test Code
```java
public class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member saveMember;
    private Email email;

    @Before
    public void setUp() throws Exception {
        final String value = "cheese10yun@gmail.com";
        email = EmailBuilder.build(value);
        final Name name = NameBuilder.build();
        saveMember = memberRepository.save(MemberBuilder.build(email, name));
    }

    ...
    @Test
    public void existsByEmail_존재하는경우_true() {
        final boolean existsByEmail = memberRepository.existsByEmail(email);
        assertThat(existsByEmail).isTrue();
    }

    @Test
    public void existsByEmail_존재하지않은_경우_false() {
        final boolean existsByEmail = memberRepository.existsByEmail(Email.of("ehdgoanfrhkqortntksdls@asd.com"));
        assertThat(existsByEmail).isFalse();
    }
}
```
* `setUp()` 메서드를 통해서 Member를 데이터베이스에 insert 합니다.
  * `setUp()` 메서드는 메번 테스트 코드가 실행되기전에 실행됩니다. 즉 테스트 코드 실핼 할 때마다 insert -> rollback이 자동으로 이루어집니다.
* 추가 작성한 쿼리메서드 `existsByEmail`을 테스트 진행합니다. 
  * 실제로 작성된 쿼리가 어떻게 출력되는지 `show-sql` 옵션을 통해서 확인 합니다. ORM은 SQL을 직접 장성하지 않으니 실제 쿼리가 어떻게 출력되는지 확인하는 습관을 반드시 가져야합니다.

# POJO 테스트

```java
```

## 장점

## 단점

## Code