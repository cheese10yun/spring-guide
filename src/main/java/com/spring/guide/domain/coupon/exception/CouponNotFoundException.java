package com.spring.guide.domain.coupon.exception;


import com.spring.guide.global.error.exception.EntityNotFoundException;

public class CouponNotFoundException extends EntityNotFoundException {

    public CouponNotFoundException(String target) {
        super(target + "is not found");
    }
}
