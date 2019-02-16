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
* API를 테스트할 경우 요청부터 응답까지 전체적인 테스트 실행 가능


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
* `@ActiveProfiles(TestProfile.TEST)` 설정으로 테스트에 profile을 지정합니다. 각 환경별로 properties 파일을 관리하듯이 test도 반드시 별도의 properties 파일로 관리하는 것이 바람직합니다.
* 인터페이스나 enum 클래스를 통해서 profile을 관리합니다. 오타 실수를 줄일수 있으며 전체적인 프로필이 몇개 있는지 한번에 확인할수 있습니다.
* `@Transactional` 트랜잭션 어노테이션을 추가하면 테스트코드의 데이터베이스 정보가 자동으로 Rollback됩니다. 베이스 클래스에 이 속성을 추가 해야지 실수 없이 진행할 수 있습니다.
* `@Transactional`을 추가하면 자연스럽게 데이터베이스 상태의존 적인 테스트를 자연스럽게 하지 않을 수 있게 됩니다.
* 통합 테스트시 필요한 기능들을 `protected` 으로 제공해줄 수 있습니다. API 테스트를 주로 하게되니 ObjectMapper 등 을 제공해줄수 있습니다. 유틸성 메서드들도 `protected` 으로 제공해주면 중복 코드 및 테스트 코드의 편의성이 높아집니다.
* 실제로 동작할 필요가 없으니 `@Ignore` 어노테이션을 추가합니다.

### 



# 서비스 테스트

## 장점

## 단점

## Code


# Mock API 테스트

## 장점

## 단점

## Code


# Repository Test

## 장점

## 단점

## Code


# POJO 테스트

## 장점

## 단점

## Code