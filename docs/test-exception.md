# Exception Guide


# 목차

# HTTP Status Code 정의

# 통일된 Error Response 객체

Error Reponse 객체는 항상 동일한 Error Response를 가져야합니다. 그렇지 않으면 클라이언트에서 예외 처리를 항상 동일한 로직으로 처리하 어렵습니다. Error Response 객체를 유연하게 처리하기 위해서 간혹 `Map<Key, Value>` 형식으로 처리 하는데 이는 좋지 않다고 생각합니다. 우선 Map 이라는 친구는 런타입시에 정확한 형태를 갖추기 때문에 객체를 처리하는 개발자들도 정확히 무슨 키에 무슨 데이터가 있는지 확인하기 어렵습니다.


```java
@ExceptionHandler(MethodArgumentNotValidException.class)
protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException", e);
    final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```
위 예제 코드 처럼 리턴 타입이 `ResponseEntity<ErrorResponse>` 으로 무슨 데이터가 어떻게 있는지 명확하게 추론하기 쉽도록 구성하는게 바람직합니다.

## Error Reponse JSON
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
* errors : 요청값에 대한 `field`, `value`, `reason` 작성합니다. 일반적으로 `@Valid` 어노테이션으로 `JSR 303: Bean Validation`에대한 검증을 진행합니다.
  * 만약 errors에 바인인된 결과가 없을 경우 null이 아니라 빈배열 `[]`을 응답해줍니다. null 객체는 절대 리턴하지 않습니다. null이 의미하는 것이 애매합니다. 
* code : 에러에 할당되는 유니크한 코드값 입니다.


## Error Reponse 객체

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
ErrorResponse 객체에 입니다. POJO 객체로 관리하면 `errorReponse.getXXX();` 이렇게 명확하게 객체에 있는 값을 가져올 수 있습니다. 그 밖에 특정 Exception에 대해서 ErrorReponse 객체를 어떻게 만들 것인가에 대한 책임을 명확하게 갖는 구조로 설계할 수 있습니다. 세부적인 것은 코드를 확인해주세요

# @ControllerAdvice

`@ControllerAdvice` 어노테이션으로 모든 예외를 한 곳에서 처리할 수 있습니다. 해당 코드의 세부적인 것은 중요하지 않으며 가장 기본적이며 필수적으로 처리하는 코드입니다. 코드에 대한 이해보다 아래의 설명을 참고하는게 좋습니다.

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
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
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
  * avax.validation.Valid or @Validated 으로 binding error 발생시 발생한다. ) 
  * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생 주로 @RequestBody, @RequestPart 어노테이션에서 발생
* handleBindException
  * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
* MethodArgumentTypeMismatchException
  * enum type 일치하지 않아 binding 못할 경우 발생
  * 주로 @RequestParam enum으로 binding 못했을 경우 발생
* handleHttpRequestMethodNotSupportedException :
  * 지원하지 않은 HTTP method 호출 할 경우 발생
* handleAccessDeniedException
  * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
  * Security에서 던지는 예외
* handleException 
  * 그 밖에 발생하는 모든 예외 처리, Null Point Exception, 등등
  * 개발자가 직접 핸들링해서 다른 예외로 던지지 않으면 모두 이곳으로 모인다.
* handleBusinessException
  * 비지니스 요규사항에 따른 Exception
  * 아래에서 자세한 설명 진행

추가적으로 스프링 및 라이브러리 등 자체적으로 발생하는 예외는 `@ExceptionHandler` 으로 추가해서 적절한 Error Reponse를 만들고 **비지니스 요구사항에 예외일 경우 `BusinessException` 으로 통일성 있게 처리하는 것을 목표로 한다. 추가적으로 늘어날수는 있겠지만 그 갯수를 최소한으로 하는 노력이 필요합니다.**


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

에러 코드가 전체적으로 흩어져있을 경우 코드, 메시지의 중복을 방지하기 어렵고 전체적으로 관리하는 것이 매우 어렵습니다. `C001` 같은 코드도 동일하게 enum으로 관리리 하는 것도 좋습니다. 에러 메시지는 Common과 각 도메인 별로 관리하는 것이 효율적일거 같습니다.

# Business Exception 처리





