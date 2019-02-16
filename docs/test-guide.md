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
@ActiveProfiles("test")
@Transactional
@Ignore
public class IntegrationTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ResourceLoader resourceLoader;

    protected <T> T readValue(final String path, Class<T> clazz) throws IOException {
        final InputStream json = resourceLoader.getResource(path).getInputStream();
        return objectMapper.readValue(json, clazz);
    }

    protected String readJson(final String path) throws IOException {
        final InputStream inputStream = resourceLoader.getResource(path).getInputStream();
        final ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() {
                return inputStream;
            }
        };
        return byteSource.asCharSource(Charsets.UTF_8).read();
    }
}
```


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


# 지속성
* 지속적인 테스트 커버리지
* 단순한것도 테스트 짜는 습관
* 