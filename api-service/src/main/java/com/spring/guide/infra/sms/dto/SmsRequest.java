package com.spring.guide.infra.sms.dto;


import lombok.Getter;

@Getter
public class SmsRequest {

  private String senderNumber;
  private String message;

}
