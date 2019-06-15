package com.spring.guide.infra.sms;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.spring.guide.infra.sms.dto.SmsRequest;
import org.junit.Test;

public class AmazonSmsClientTest {

  @Test
  public void send_test() {
    final SmsClient smsClient = new AmazonSmsClient();
    smsClient.send(new SmsRequest());
  }
}