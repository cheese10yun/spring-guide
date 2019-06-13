package com.spring.guide.domain.coupon;

import com.spring.guide.global.error.exception.BusinessException;
import com.spring.guide.global.error.exception.ErrorCode;

public class CouponAlreadyUseException extends BusinessException {

  public CouponAlreadyUseException() {
    super(ErrorCode.COUPON_ALREADY_USE);
  }

}
