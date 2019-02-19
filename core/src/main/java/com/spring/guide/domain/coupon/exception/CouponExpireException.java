package com.spring.guide.domain.coupon.exception;

import com.spring.guide.excpetion.ErrorCode;
import com.spring.guide.excpetion.InvalidValueException;

public class CouponExpireException extends InvalidValueException {

    public CouponExpireException() {
        super(ErrorCode.COUPON_EXPIRE);
    }
}
