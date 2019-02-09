package com.spring.guide.domain.member.exception;

import com.spring.guide.excpetion.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException(Long target) {
        super(target + " is not found");
    }
}
