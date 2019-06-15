package com.spring.guide.global.config.resttemplate;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

  private final RestTemplateBuilder restTemplateBuilder;

  @Bean
  public RestTemplate localTestTemplate() {
    return restTemplateBuilder.rootUri("http://localhost:8899")
        .additionalInterceptors(new RestTemplateClientHttpRequestInterceptor())
        .errorHandler(new RestTemplateErrorHandler())
        .setConnectTimeout(Duration.ofMinutes(3))
        .build();
  }


  @Bean
  public RestTemplate amazonSmsTemplate() {
    return restTemplateBuilder.rootUri("http://localhost:8899")
        .additionalInterceptors(new RestTemplateClientHttpRequestInterceptor())
        .errorHandler(new RestTemplateErrorHandler())
        .setConnectTimeout(Duration.ofMinutes(3))
        .build();
  }
}
