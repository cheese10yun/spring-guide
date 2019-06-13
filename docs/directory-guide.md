# Directory Guide 

패키지 구성은 크게 레이어 계층형, 도메인형 이렇게 2 가지 유형이 있다고 생각합니다. 각 유형별로 간단하게 설명하고 제 개인적인 Best Practices를 설명하겠습니다.

## 계층형 
```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── demo
    │   │               ├── DemoApplication.java
    │   │               ├── config
    │   │               ├── controller
    │   │               ├── dao
    │   │               ├── domain
    │   │               ├── exception
    │   │               └── service
    │   └── resources
    │       └── application.properties
```

계층형 구조는 각 계층을 대표하는 디렉토리를 기준으로 코드들이 구성됩니다. 계층형 구조의 장점은 해당 프로젝트에 이해가 상대적으로 낮아도 전체적인 구조를 빠르게 파악할 수 있는 장점이 있습니다. 단점으로는 디렉토리에 클래스들이 너무 많이 모이게 되는 점입니다.

## 도메인형
```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── demo
    │   │               ├── DemoApplication.java
    │   │               ├── coupon
    │   │               │   ├── controller
    │   │               │   ├── domain
    │   │               │   ├── exception
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── member
    │   │               │   ├── controller
    │   │               │   ├── domain
    │   │               │   ├── exception
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               └── order
    │   │                   ├── controller
    │   │                   ├── domain
    │   │                   ├── exception
    │   │                   ├── repository
    │   │                   └── service
    │   └── resources
    │       └── application.properties

```
도메인 디렉토리 기준으로 코드를 구성합니다. 도메인형의 장점은 관련된 코드들이 응집해 있는 장점이 있습니다. 단점으로는 프로젝트에 대한 이해도가 낮을 경우 전체적인 구조를 파악하기 어려운점이 있습니다.


## 개인적인 Best Practices
저는 도메인형이 더 좋은 구조라고 생각합니다. 이전 부터 도메인형을 선호 했었지만 이러한 디렉토리 구조는 어느 정도 취향 차이라고 생각해 왔었습니다. 하지만 최근들어 취향 차이를 넘어 도메인형 디렉토리 구조가 더 효과적 이라고 확신이들어 이 주제로 포스팅을 해야겠다는 생각을 했습니다.

### 너무 많은 클래스
계층형같은 경우 Controller, Service 등에 너무 많은 클래스들이 밀집하게 됩니다. 많게는 30 ~ 40의 클래스들이 xxxxController, xxxxService 같은 패턴으로 길게 나열되어 프로젝트 전체적인 구조는 상단 디렉토리 몇개로 빠르게 파악할 수 있지만 그 이후로는 파악하기가 더 힘들어 지게됩니다.

### 관련 코드의 응집
관련된 코드들이 응집해 있으면 자연스럽게 연관되 있는 자연스럽게 코드 스타일, 변수, 클래스 이름 등을 참고 하게 되고 비슷한 코드 스타일과 패턴으로 개발할 수 있게될 환경이 자연스럽게 마련된다고 생각합니다.

계층형 구조일 경우 수신자에 대한 클래스명을 Receiver로 지정했다면, 너무 많은 클래스들로 Receiver에 대한 클래스가 자연스럽게 인식하지 않게 되고 Recipient 같은 클래스 명이나 네이밍을 사용하게 됩니다. 반변 도메인형은 관련된 코드들이 응집해있기 때문에 자연스럽게 기존 코드를 닮아갈 수 있다고 생각합니다. 

또 해당 디렉토리가 컨텍스트를 제공해줍니다. order 라는 디렉토리에 Receiver 클래스가 있는 경우 주문을 배송 받는 수취인이라는 컨텍스트를 제공해줄수 있습니다. (물론 OrderReceiver 라고 더 구체적으로 명명하는게 더 좋은 네이밍 이라고 생각합니다.)

### 접근지시자를 통한 제어
???


### 최근 기술 동향
도메인 주도 개발, ORM, 객체지향 프로그래밍 등에서 도메인형 구조가 더 적합하다고 생각합니다. 도메인주도 개발에서 Root Aggregate 같은 표현은 계층형보다 도메인형으로 표현했을 경우 훨씬더 직관적이며 해당 도메인을 이해하는 것에도 효율적이라고 생각합니다.


## 개인적 도메인형 계층 구조
도메인 계층으로 디렉토리 구조를 몇몇 프로젝트에서 진행해 보았지만 그 떄 마다 더 좋은 구조를 찾게 되고 조금더 발전 시키는 가정이다 보니 아직은 아주 명확한 근거를 기반으로 하지는 못하고 있습니다. 약간 코에걸면 코걸이 느낌이 있습니다. 


### 전체적인 구조

```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── spring
    │   │           └── guide
    │   │               ├── ApiApp.java
    │   │               ├── SampleApi.java
    │   │               ├── domain
    │   │               │   ├── coupon
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   ├── member
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   └── model
    │   │               │       ├── Address.java
    │   │               │       ├── Email.java
    │   │               │       └── Name.java
    │   │               ├── global
    │   │               │   ├── common
    │   │               │   │   ├── request
    │   │               │   │   └── resonse
    │   │               │   ├── config
    │   │               │   │   ├── SwaggerConfig.java
    │   │               │   │   ├── properties
    │   │               │   │   ├── resttemplate
    │   │               │   │   └── security
    │   │               │   ├── error
    │   │               │   │   ├── ErrorResponse.java
    │   │               │   │   ├── GlobalExceptionHandler.java
    │   │               │   │   └── exception
    │   │               │   └── util
    │   │               └── infra
    │   │                   ├── email
    │   │                   └── sms
    │   │                       ├── AmazonSmsClient.java
    │   │                       ├── SmsClient.java
    │   │                       └── dto
    │   └── resources
    │       ├── application-dev.yml
    │       ├── application-local.yml
    │       ├── application-prod.yml
    │       └── application.yml

```

전체적인 구조는 도메인을 담당하는 디렉토리 domain, 전체적인 설정을 관리하는 global, 외부 인프라 스트럭처를 관리하는 infra를 기준으로 설명을 드리겠습니다.

### Domain

```
├── domain
│   ├── member
│   │   ├── api
│   │   │   └── MemberApi.java
│   │   ├── application
│   │   │   ├── MemberProfileService.java
│   │   │   ├── MemberSearchService.java
│   │   │   ├── MemberSignUpRestService.java
│   │   │   └── MemberSignUpService.java
│   │   ├── dao
│   │   │   ├── MemberFindDao.java
│   │   │   ├── MemberPredicateExecutor.java
│   │   │   ├── MemberRepository.java
│   │   │   ├── MemberSupportRepository.java
│   │   │   └── MemberSupportRepositoryImpl.java
│   │   ├── domain
│   │   │   ├── Member.java
│   │   │   └── ReferralCode.java
│   │   ├── dto
│   │   │   ├── MemberExistenceType.java
│   │   │   ├── MemberProfileUpdate.java
│   │   │   ├── MemberResponse.java
│   │   │   └── SignUpRequest.java
│   │   └── exception
│   │       ├── EmailDuplicateException.java
│   │       ├── EmailNotFoundException.java
│   │       └── MemberNotFoundException.java
│   └── model
│       ├── Address.java
│       ├── Email.java
│       └── Name.java

```


`model` 디렉토리는 Domain Entity 객체들이 공통적으로 사용할 객체들로 구성됩니다. 대표적으로 `Embeddable` 갹체, `Enum` 객체 등이 있습니다.

`member` 디렉토리는 간단한 것들 부터 설명하겠습니다.

* api : 컨트롤러 클래스들이 존재합니다. 외부 rest api로 프로젝트를 구성하는 경우가 많으니 api라고 지칭했습니다. Controller 같은 경우에는 ModelAndView를 리턴하는 느낌이 있어서 명시적으로 api라고 하는게 더 직관적인거 같습니다.
* domain : 도메인 엔티티에 대한 클래스로 구성됩니다. 특정 도메인에만 속하는 `Embeddable`, `Enum` 같은 클래스도 구성됩니다.
* dto : 주로 Request, Response 객체들로 구성됩니다.
* exception : 해당 도메인이 발생시키는 Exception으로 구성됩니다.


#### application
application 디렉토리는 도메인 객체와 외부 영역을 연결해주는 파사드와 같은 역할을 주로 담당하는 클래스로 구성됩니다. 대표적으로 데이터베이스 트랜잭션을 처리를 진행합니다. service 계층과 유사합니다. 디렉토리  이름을 service로 하지 않은 이유는 service로 했을 경우 xxxxService로 클래스 네임을 해야한다는 강박관념이 생기기 때문에 application 이라고 명명했습니다.


#### dao
repository 와 비슷합니다. repository로 하지 않은 이유는 조회 전용 구현체들이 작성 많이 작성되는데 이러한 객체들은 DAO라는 표현이 더 직관적이라고 판단했습니다. [Querydsl를 이용해서 Repository 확장하기(1), (2)](https://github.com/cheese10yun/spring-jpa-best-practices) 처럼 Reopsitory를 DAO 처럼 확장하 기 때문에 dao 디렉토리 명이 더 직관적이라고 생각합니다.

### global

```
├── global
│   ├── common
│   │   ├── request
│   │   └── resonse
│   │       └── Existence.java
│   ├── config
│   │   ├── SwaggerConfig.java
│   │   ├── properties
│   │   ├── resttemplate
│   │   │   ├── RestTemplateClientHttpRequestInterceptor.java
│   │   │   ├── RestTemplateConfig.java
│   │   │   └── RestTemplateErrorHandler.java
│   │   └── security
│   ├── error
│   │   ├── ErrorResponse.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── exception
│   │       ├── BusinessException.java
│   │       ├── EntityNotFoundException.java
│   │       ├── ErrorCode.java
│   │       └── InvalidValueException.java
│   └── util
```

global은 프로젝트 전방위적으로 사용되는 객체들로 구성됩니다. global로 지정한 이유는 common, util, config등 프로젝트 전체에서 사용되는 클래스들이 global 이라는 디렉토리에 모여 있는것이 좋다고 생각했습니다.

* common : 공통으로 사용되는 Value 객체들로 구성됩니다. 페이징 처리를 위한 Request, 공통된 응답을 주는 Response 객체들이 있습니다. 
* config : 스프링 각종 설정들로 구성됩니다. 
* error : 예외 헨들링을 담당하는 클래스로 구성됩니다.  [Exception Guide](https://github.com/cheese10yun/spring-guide/blob/master/docs/exception-guide.md)에서 설명했던 코드들이 있습니다.
* util : 유틸성 클래스들이 위치합니다.


### infra
