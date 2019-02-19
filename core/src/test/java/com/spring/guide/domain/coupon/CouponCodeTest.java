package com.spring.guide.domain.coupon;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class CouponCodeTest {

    @Test
    public void generateCode() {
        final CouponCode code = CouponCode.generateCode();
        assertThat(code.getValue().length()).isEqualTo(10);
    }

    @Test
    public void of() {
        final CouponCode code = CouponCode.of("0123456789");
        assertThat(code).isInstanceOf(CouponCode.class);
    }

    @Test
    public void getValue() {
        final String value = "0123456789";
        final CouponCode code = CouponCode.of(value);
        assertThat(code.getValue()).isEqualTo(value);
    }
}