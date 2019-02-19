package com.spring.guide.excpetion;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
