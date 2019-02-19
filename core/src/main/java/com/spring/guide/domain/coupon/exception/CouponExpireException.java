package com.spring.guide.domain.coupon.exception;

import com.spring.guide.excpetion.BusinessException;
import com.spring.guide.excpetion.ErrorCode;

public class CouponExpireException extends BusinessException {

    public CouponExpireException() {
        super(ErrorCode.COUPON_EXPIRE);
    }
}
