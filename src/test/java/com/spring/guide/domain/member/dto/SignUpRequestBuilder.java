package com.spring.guide.domain.member.dto;

import com.spring.guide.domain.model.Email;
import com.spring.guide.domain.model.Name;

public class SignUpRequestBuilder {

  public static SignUpRequest build(Email email, Name name) {
    return new SignUpRequest(email, name);
  }


}