package com.spring.guide.member.exception;


import com.spring.guide.excpetion.ErrorCode;
import com.spring.guide.global.error.exception.InvalidValueException;
import com.spring.guide.model.Email;

public class EmailDuplicateException extends InvalidValueException {

    public EmailDuplicateException(final Email email) {
        super(email.getValue(), ErrorCode.EMAIL_DUPLICATION);
    }
}
