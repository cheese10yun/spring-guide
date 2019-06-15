package com.spring.guide.domain.coupon.exception;


import com.spring.guide.global.error.exception.BusinessException;
import com.spring.guide.global.error.exception.ErrorCode;

public class CouponExpireException extends BusinessException {

  public CouponExpireException() {
    super(ErrorCode.COUPON_EXPIRE);
  }
}
