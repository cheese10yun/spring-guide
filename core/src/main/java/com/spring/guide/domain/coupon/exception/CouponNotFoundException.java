package com.spring.guide.domain.coupon.exception;


import com.spring.guide.excpetion.EntityNotFoundException;

public class CouponNotFoundException extends EntityNotFoundException {

    public CouponNotFoundException(String target) {
        super(target + "is not found");
    }
}
