package com.spring.guide.domain.member.exception;


import com.spring.guide.excpetion.EntityNotFoundException;

public class EmailNotFoundException extends EntityNotFoundException {

    public EmailNotFoundException(String target) {
        super(target + " is not found");
    }
}
