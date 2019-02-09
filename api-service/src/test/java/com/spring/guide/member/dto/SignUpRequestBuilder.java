package com.spring.guide.member.dto;

import com.spring.guide.domain.member.Member;
import com.spring.guide.model.Email;
import com.spring.guide.model.Name;

import static org.junit.Assert.*;

public class SignUpRequestBuilder {

    public static SignUpRequest build(Email email, Name name){
        return new SignUpRequest(email, name);
    }



}