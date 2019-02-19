package com.spring.guide.coupon;

import com.spring.guide.domain.coupon.Coupon;
import com.spring.guide.domain.coupon.CouponCode;
import com.spring.guide.domain.coupon.CouponHelperService;
import com.spring.guide.domain.coupon.exception.CouponExpireException;
import com.spring.guide.test.MockTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class CouponUseServiceTest extends MockTest {

    @InjectMocks
    private CouponUseService couponUseService;

    @Mock
    private CouponHelperService couponHelperService;


    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void 쿠폰사용시_used_is_true() {
        //given
        final Coupon coupon = CouponBuilder.build();
        final CouponCode code = CouponCode.generateCode();

        given(couponHelperService.findByCode(any())).willReturn(coupon);

        //when
        couponUseService.use(code);

        //then
        assertThat(coupon.isUsed()).isTrue();
    }

    @Test(expected = CouponExpireException.class)
    public void 쿠폰만료된거_사용시_예외() {

        //given
        final Coupon coupon = CouponBuilder.build(LocalDate.now().minusDays(10));
        final CouponCode code = CouponCode.generateCode();
        given(couponHelperService.findByCode(code)).willReturn(coupon);

        //when
        couponUseService.use(code);

        //then


    }
}