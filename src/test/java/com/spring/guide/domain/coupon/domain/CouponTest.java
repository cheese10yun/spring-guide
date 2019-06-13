package com.spring.guide.domain.coupon.domain;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.spring.guide.domain.coupon.CouponBuilder;
import com.spring.guide.domain.coupon.exception.CouponExpireException;
import java.time.LocalDate;
import org.junit.Test;

public class CouponTest {

    @Test(expected = CouponExpireException.class)
    public void 만료일이지난_쿠폰_사용_불가() {
        final Coupon coupon = CouponBuilder.build(LocalDate.now().minusDays(1));
        coupon.use();
    }

    @Test
    public void 사용가능_쿠폰_사용시_used_is_true() {
        final Coupon coupon = CouponBuilder.build(LocalDate.now().plusDays(1));
        coupon.use();
        assertThat(coupon.isUsed()).isTrue();
    }


}