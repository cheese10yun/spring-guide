package com.spring.guide.infra.sms;

import com.spring.guide.infra.sms.dto.SmsRequest;

public interface SmsClient {

  void send(SmsRequest dto);

}
