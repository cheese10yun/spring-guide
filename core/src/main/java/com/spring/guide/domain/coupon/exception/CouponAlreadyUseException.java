package com.spring.guide.domain.coupon.exception;

import com.spring.guide.excpetion.ErrorCode;
import com.spring.guide.excpetion.InvalidValueException;

public class CouponAlreadyUseException extends InvalidValueException {

    public CouponAlreadyUseException() {
        super(ErrorCode.COUPON_ALREADY_USE);
    }

}
