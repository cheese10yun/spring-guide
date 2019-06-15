package com.spring.guide.infra.sms;

import com.spring.guide.infra.sms.dto.SmsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KtSmsClient implements SmsClient {

  @Override
  public void send(SmsRequest dto) {
    // Kt sms 호출...
  }
}
