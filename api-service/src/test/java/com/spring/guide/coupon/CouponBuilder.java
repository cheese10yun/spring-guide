package com.spring.guide.coupon;


import com.spring.guide.domain.coupon.Coupon;
import com.spring.guide.domain.coupon.CouponCode;

import java.time.LocalDate;

public class CouponBuilder {

    public static Coupon build(LocalDate expirationDate) {
        return Coupon.builder()
                .code(CouponCode.generateCode())
                .discount(10)
                .expirationDate(expirationDate)
                .build();
    }

    public static Coupon build() {
        return Coupon.builder()
                .code(CouponCode.generateCode())
                .discount(10)
                .expirationDate(LocalDate.now().plusDays(20))
                .build();
    }

}