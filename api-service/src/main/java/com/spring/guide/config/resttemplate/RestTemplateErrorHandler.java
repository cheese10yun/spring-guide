package com.spring.guide.config.resttemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
public class RestTemplateErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(@NonNull final ClientHttpResponse response) throws IOException {
    final HttpStatus statusCode = response.getStatusCode();
//    response.getBody() 넘겨 받은 body 값으로 적절한 예외 상태 확인 이후 boolean return
    return !statusCode.is2xxSuccessful();
  }

  @Override
  public void handleError(@NonNull final ClientHttpResponse response) throws IOException {
//    hasError에서 true를 return하면 해당 메서드 실행.
//    상황에 알맞는 Error handling 로직 작성....
    final String error = getErrorAsString(response);
    log.error("================");
    log.error("Headers: {}", response.getHeaders());
    log.error("Response Status : {}", response.getRawStatusCode());
    log.error("Request body: {}", error);
    log.error("================");

  }

  private String getErrorAsString(@NonNull final ClientHttpResponse response) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody()))) {
      return br.readLine();
    }
  }
}

