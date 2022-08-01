# Exception Guide
스프링은 예외처리를 위해 다양하고 막강한 어노테이션을 제공하고 있습니다. 일관성 있는 코드 스타일을 유지하면서 Exception을 처리하는 방법에 대해서 소개하겠습니다.

# 목차
- [Exception Guide](#exception-guide)
- [목차](#%EB%AA%A9%EC%B0%A8)
- [통일된 Error Response 객체](#%ED%86%B5%EC%9D%BC%EB%90%9C-error-response-%EA%B0%9D%EC%B2%B4)
  - [Error Response JSON](#error-reponse-json)
  - [Error Response 객체](#error-reponse-%EA%B0%9D%EC%B2%B4)
- [@ControllerAdvice로 모든 예외를 핸들링](#controlleradvice-%EB%AA%A8%EB%93%A0-%EC%98%88%EC%99%B8%EB%A5%BC-%ED%97%A8%EB%93%A4%EB%A7%81)
- [Error Code 정의](#error-code-%EC%A0%95%EC%9D%98)
- [Business Exception 처리](#business-exception-%EC%B2%98%EB%A6%AC)
  - [비즈니스 예외를 위한 최상위 BusinessException 클래스](#%EB%B9%84%EC%A7%80%EB%8B%88%EC%8A%A4-%EC%98%88%EC%99%B8%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%B5%9C%EC%83%81%EC%9C%84-businessexception-%ED%81%B4%EB%9E%98%EC%8A%A4)
  - [Coupon Code](#coupon-code)
- [컨트롤러 예외 처리](#%EC%BB%A8%ED%8A%B8%EB%A1%A4%EB%9F%AC-%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC)
  - [Controller](#controller)
- [Try Catch 전략](#try-catch-%EC%A0%84%EB%9E%B5)

# 통일된 Error Response 객체

Error Response 객체는 항상 동일한 Error Response를 가져야 합니다. 그렇지 않으면 클라이언트에서 예외 처리를 항상 동일한 로직으로 처리하기 어렵습니다. Error Response 객체를 유연하게 처리하기 위해서 간혹 `Map<Key, Value>` 형식으로 처리하는데 이는 좋지 않다고 생각합니다. 우선 Map 이라는 친구는 런타임시에 정확한 형태를 갖추기 때문에 객체를 처리하는 개발자들도 정확히 무슨 키에 무슨 데이터가 있는지 확인하기 어렵습니다.

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException", e);
    final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```
위 예제 코드처럼 리턴 타입이 `ResponseEntity<ErrorResponse>` 으로 무슨 데이터가 어떻게 있는지 명확하게 추론하기 쉽도록 구성하는 게 바람직합니다.

## Error Response JSON
```json
{
  "message": " Invalid Input Value",
  "status": 400,
  // "errors":[], 비어있을 경우 null 이 아닌 빈 배열을 응답한다.
  "errors": [
    {
      "field": "name.last",
      "value": "",
      "reason": "must not be empty"
    },
    {
      "field": "name.first",
      "value": "",
      "reason": "must not be empty"
    }
  ],
  "code": "C001"
}
```
ErrorResponse 객체의 JSON 입니다.
* message : 에러에 대한 message를 작성합니다.
* status : http status code를 작성합니다. header 정보에도 포함된 정보이니 굳이 추가하지 않아도 됩니다.
* errors : 요청 값에 대한 `field`, `value`, `reason` 작성합니다. 일반적으로 `@Valid` 어노테이션으로 `JSR 303: Bean Validation`에 대한 검증을 진행 합니다.
  * 만약 errors에 바인인된 결과가 없을 경우 null이 아니라 빈 배열 `[]`을 응답해줍니다. null 객체는 절대 리턴하지 않습니다. null이 의미하는 것이 애매합니다.
* code : 에러에 할당되는 유니크한 코드값입니다.


## Error Response 객체

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private int status;
    private List<FieldError> errors;
    private String code;
    ...

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
        ...
    }
}
```
ErrorResponse 객체 입니다. POJO 객체로 관리하면 `errorResponse.getXXX();` 이렇게 명확하게 객체에 있는 값을 가져올 수 있습니다. 그 밖에 특정 Exception에 대해서 ErrorResponse 객체를 어떻게 만들 것인가에 대한 책임을 명확하게 갖는 구조로 설계할 수 있습니다. 세부적인 것은 코드를 확인해주세요.

# @ControllerAdvice로 모든 예외를 핸들링

`@ControllerAdvice` 어노테이션으로 모든 예외를 한 곳에서 처리할 수 있습니다. 해당 코드의 세부적인 것은 중요하지 않으며 가장 기본적이며 필수적으로 처리하는 코드입니다. 코드에 대한 이해보다 아래의 설명을 참고하는 게 좋습니다.

```java
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합니다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleEntityNotFoundException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleEntityNotFoundException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

* handleMethodArgumentNotValidException
  * avax.validation.Valid or @Validated 으로 binding error 발생 시 발생한다. )
  * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할 경우 발생. 주로 @RequestBody, @RequestPart 어노테이션에서 발생함.
* handleBindException
  * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
* MethodArgumentTypeMismatchException
  * enum type 일치하지 않아 binding 못할 경우 발생
  * 주로 @RequestParam enum으로 binding 못했을 경우 발생
* handleHttpRequestMethodNotSupportedException :
  * 지원하지 않은 HTTP method 호출 할 경우 발생
* handleAccessDeniedException
  * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합니다.
  * Security에서 던지는 예외
* handleException
  * 그 밖에 발생하는 모든 예외 처리, Null Point Exception, 등등
  * 개발자가 직접 핸들링해서 다른 예외로 던지지 않으면 모두 이곳으로 모인다.
* handleBusinessException
  * 비즈니스 요규사항에 따른 Exception
  * 아래에서 자세한 설명 진행

추가로 스프링 및 라이브러리 등 자체적으로 발생하는 예외는 `@ExceptionHandler` 으로 추가해서 적절한 Error Response를 만들고 **비지니스 요구사항에 예외일 경우 `BusinessException` 으로 통일성 있게 처리하는 것을 목표로 한다. 추가로 늘어날 수는 있겠지만 그 개수를 최소한으로 하는 노력이 필요합니다.**


# Error Code 정의
```java
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ....
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

    ;
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
```
에러 코드는 enum 타입으로 한 곳에서 관리합니다.

에러 코드가 전체적으로 흩어져있을 경우 코드, 메시지의 중복을 방지하기 어렵고 전체적으로 관리하는 것이 매우 어렵습니다. `C001` 같은 코드도 동일하게 enum으로 관리 하는 것도 좋습니다. 에러 메시지는 Common과 각 도메인별로 관리하는 것이 효율적일 거 같습니다.

# Business Exception 처리
여기서 말하는 Business Exception은 요구사항에 맞지 않을 경우 발생시키는 Exception을 말합니다. 만약 쿠폰을 사용 하려고 하는데 이미 사용한 쿠폰인 경우에는 더 이상 정상적인 흐름을 이어갈수가 없게 됩니다. 이런 경우에는 적절한 Exception을 발생시키고 로직을 종료 시켜야합니다.

더 쉽게 정리하면 요구사항에 맞게 개발자가 직접 Exception을 발생시키는 것들이 Business Exception 이라고 할수 있습니다.

유지 보수하기 좋은 코드를 만들기 위해서는 Exception을 발생시켜야 합니다. 쿠폰을 입력해서 상품을 주문했을 경우 상품 계산 로직에서 이미 사용해 버린 쿠폰이면 로직을 이어나가기는 어렵습니다.

단순히 어려운 것이 아니라 해당 계산 로직의 책임이 증가하게 됩니다. 계산 로직은 특정 공식에 의해서 제품의 가격을 계산하는 것이 책임이지 쿠폰이 이미 사용 해 경우, 쿠폰이 만료되었을 경우, 제품이 매진 됐을 경우 등등의 책임을 갖게 되는 순간 유지 보수하기 어려운 코드가 됩니다. 객체의 적절한 책임을 주기 위해서라도 본인이 처리 못 하는 상황일 경우 적절한 Exception을 발생시켜야 합니다.

> 클린 코드 : 오류 코드 보다 예외를 사용하라 리팩토링

```java
public class DeviceController {
    ...
    public void sendShutDown() {
        DeviceHandle handle = getHandle(DEV1);
        // 디바이스 상태를 점검한다.
        if (handle != DeviceHandle.INVALID) {
            // 레코드 필드에 디바이스 상태를 저장한다.
            retrieveDeviceRecord(handle);
            // 디바이스가 일시정지 상태가 아니라면 종료한다.
            if (record.getStatus() != DEVICE_SUSPENDED) {
                pauseDevice(handle);
                clearDeviceWorkQueue(handle);
                closeDevice(handle);
            } else {
                logger.log("Device suspended. Unable to shut down");
            }
        } else {
            logger.log("Invalid handle for: " + DEV1.toString());
        }
    }
    ...
}
```
`if ... else`의 반복으로 인해서 sendShutDown 핵심 비지니스 코드의 이해하기가 어렵습니다.

```java
public class DeviceController {
    ...
    public void sendShutDown() {
        try {
            tryToShutDown();
        } catch (DeviceShutDownError e) {
            logger.log(e);
        }
    }

    private void tryToShutDown() throws DeviceShutDownError {
        DeviceHandle handle = getHandle(DEV1);
        DeviceRecord record = retrieveDeviceRecord(handle);
        pauseDevice(handle);
        clearDeviceWorkQueue(handle);
        closeDevice(handle);
    }

    private DeviceHandle getHandle(DeviceID id) {
        ...
        throw new DeviceShutDownError("Invalid handle for: " + id.toString());
        ...
    }
    ...
}
```
객체 본인의 책임 외적인 것들은 DeviceShutDownError 예외를 발생시키고 있습니다. 코드의 가독성과 책임이 분명하게 드러나고 있습니다.

## 비지니스 예외를 위한 최상위 BusinessException 클래스

![](/docs/imgs/BusinessException-final.png)

최상위 BusinessException을 상속 받는 InvalidValueException, EntityNotFoundExceptuon 등이 있습니다.

* InvalidValueException : 유효하지 않은 값일 경우 예외를 던지는 Excetion
  * 쿠폰 만료, 이미 사용한 쿠폰 등의 이유로 더이상 진행이 못할경우
* EntityNotFoundException : 각 엔티티들을 못찾았을 경우
  * `findById`, `findByCode` 메서드에서 조회가 안되었을 경우

최상위 BusinessException을 기준으로 예외를 발생시키면 통일감 있는 예외 처리를 가질 수 있습니다. 비니지스 로직을 수행하는 코드 흐름에서 로직의 흐름을 진행할 수 없는 상태인 경우에는 적절한 BusinessException 중에 하나를 예외를 발생 시키거나 직접 정의하게 됩니다.

```java
@ExceptionHandler(BusinessException.class)
protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    log.error("handleEntityNotFoundException", e);
    final ErrorCode errorCode = e.getErrorCode();
    final ErrorResponse response = ErrorResponse.of(errorCode);
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
}
```
이렇게 발생하는 모든 예외는 `handleBusinessException` 에서 동일하게 핸들링 됩니다. 예외 발생시 알람을 받는 등의 추가적인 행위도 손쉽게 가능합니다. 또 BusinessException 클래스의 하위 클래스 중에서 특정 예외에 대해서 다른 알람을 받는 등의 더 디테일한 핸들링도 가능해집니다.


## Coupon Code

```java
public class Coupon {

    ...

    public void use() {
        verifyExpiration();
        verifyUsed();
        this.used = true;
    }

    private void verifyUsed() {
        if (used) throw new CouponAlreadyUseException();
    }

    private void verifyExpiration() {
        if (LocalDate.now().isAfter(getExpirationDate())) throw new CouponExpireException();
    }
}
```
쿠폰의 `use` 메서드입니다. 만료일과 사용 여부를 확인하고 예외가 발생하면 적절한 Exception을 발생시킵니다.

# 컨트롤러 예외 처리

컨틀롤러에서 모든 요청에 대한 값 검증을 진행하고 이상이 없을 시에 서비스 레이어를 호출해야 합니다. 위에서도 언급했듯이 잘못된 값이 있으면 서비스 레이어에서 정상적인 작업을 진행하기 어렵습니다. **무엇보다 컨틀롤러의 책임을 다하고 있지 않으면 그 책임은 자연스럽게 다른 레이어로 전해지게 되며 이렇게 넘겨받은 책임을 처리하는데 큰 비용과 유지보수 하기 어려워질 수밖에 없습니다.**

컨트롤러의 중요한 책임 중의 하나는 요청에 대한 값 검증이 있습니다. 스프링은 JSR 303 기반 어노테이션으로 값 검증을 쉽고 일관성 있게 처리할 수 있도록 도와줍니다. 모든 예외는 `@ControllerAdvice` 선언된 객체에서 핸들링 됩니다. 컨트롤러로 본인이 직접 예외까지 처리하지 않고 예외가 발생하면 그냥 던져버리는 패턴으로 일관성 있게 개발할 수 있습니다.


## Controller
```java
@RestController
@RequestMapping("/members")
public class MemberApi {

    private final MemberSignUpService memberSignUpService;

    @PostMapping
    public MemberResponse create(@RequestBody @Valid final SignUpRequest dto) {
        final Member member = memberSignUpService.doSignUp(dto);
        return new MemberResponse(member);
    }
}
```
```java
public class SignUpRequest {
    @Valid private Email email;
    @Valid private Name name;
}

public class Name {
    @NotEmpty private String first;
    private String middle;
    @NotEmpty private String last;
}

public class Email {
    @javax.validation.constraints.Email
    private String value;
}
```

회원 가입 Reuqest Body 중에서 유효하지 않은 값이 있을 때 `@Valid` 어노테이션으로 예외를 발생시킬 수 있습니다. 이 예외는 `@ControllerAdvice`에서 적절하게 처리됩니다. `@NotEmpty`, `@Email` 외에도 다양한 어노테이션들이 제공됩니다.

# Try Catch 전략
기본적으로 예외가 발생하면 로직의 흐름을 끊고 종료 시켜야 합니다. 물론 예외도 있지만, 최대한 예외를 발생시켜 종료하는 것을 지향해야 한다고 생각합니다.

```java
try {
    // 비즈니스 로직 수행...
}catch (Exception e){
    e.printStackTrace();
}
```
위 같은 코드는 지양해야 하는 패턴입니다. 최소한의 양심으로 `e.printStackTrace();` 로그라도 출력했지만 이미 예외가 발생했음에도 불구하고 다음 로직을 실행합니다. 이런 식의 `try catch`를 최대한 지양해야 합니다.

하지만 Checked Exception 같은 경우에는 예외를 반드시 감싸야 하므로 이러한 경우에는 `try catch`를 사용해야 합니다.

```java
try {
    // 비즈니스 로직 수행...
}catch (Exception e){
    e.printStackTrace();
    throw new XXX비즈니스로직예외(e);
}
```
`try catch`를 사용해야 하는 경우라면 더 구체적인 예외로 Exception을 발생시키는 것이 좋습니다. 간단하게 정리하면

1. `try catch`를 최대한 지양해라
2. `try catch`로 에러를 먹고 주는 코드는 지양해라(이런 코드가 있다면 로그라도 추가해주세요...)
3. `try catch`를 사용하게 된다면 더 구체적인 Exception을 발생시키는 것이 좋다.
