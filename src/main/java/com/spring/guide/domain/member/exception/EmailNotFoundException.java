package com.spring.guide.domain.member.exception;


import com.spring.guide.global.error.exception.EntityNotFoundException;

public class EmailNotFoundException extends EntityNotFoundException {

    public EmailNotFoundException(String target) {
        super(target + " is not found");
    }
}
