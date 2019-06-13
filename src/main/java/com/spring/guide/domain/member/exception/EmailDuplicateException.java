package com.spring.guide.domain.member.exception;


import com.spring.guide.domain.model.Email;
import com.spring.guide.global.error.exception.ErrorCode;
import com.spring.guide.global.error.exception.InvalidValueException;

public class EmailDuplicateException extends InvalidValueException {

    public EmailDuplicateException(final Email email) {
        super(email.getValue(), ErrorCode.EMAIL_DUPLICATION);
    }
}
