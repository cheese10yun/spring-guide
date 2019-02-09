package com.spring.guide.error.exception;

import com.spring.guide.excpetion.BusinessException;
import com.spring.guide.excpetion.ErrorCode;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
