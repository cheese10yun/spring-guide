package com.spring.guide.infra.sms;

import com.spring.guide.infra.sms.dto.SmsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AmazonSmsClient implements SmsClient {

  private final RestTemplate amazonSmsTemplate;

  @Override
  public void send(SmsRequest dto) {
    amazonSmsTemplate.postForEntity("/sms", dto, Void.class);
  }
}
