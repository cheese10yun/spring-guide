package com.spring.guide.domain.coupon.dto;

import com.spring.guide.domain.coupon.Coupon;
import com.spring.guide.domain.coupon.CouponCode;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CouponResponse {

    private final CouponCode code;
    private final double discount;
    private final LocalDate expirationDate;
    private final boolean expiration;

    public CouponResponse(final Coupon coupon) {
        this.code = coupon.getCode();
        this.discount = coupon.getDiscount();
        this.expirationDate = coupon.getExpirationDate();
        this.expiration = coupon.isExpiration();
    }
}
