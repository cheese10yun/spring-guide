package com.spring.guide.domain.coupon.application;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.spring.guide.domain.coupon.CouponBuilder;
import com.spring.guide.domain.coupon.dao.CouponFindDao;
import com.spring.guide.domain.coupon.domain.Coupon;
import com.spring.guide.domain.coupon.domain.CouponCode;
import com.spring.guide.domain.coupon.exception.CouponExpireException;
import com.spring.guide.test.MockTest;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CouponUseServiceTest extends MockTest {

    @InjectMocks
    private CouponUseService couponUseService;

    @Mock
    private CouponFindDao couponFindDao;


    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void 쿠폰사용시_used_is_true() {
        //given
        final Coupon coupon = CouponBuilder.build();
        final CouponCode code = CouponCode.generateCode();

        given(couponFindDao.findByCode(any())).willReturn(coupon);

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
        given(couponFindDao.findByCode(code)).willReturn(coupon);

        //when
        couponUseService.use(code);

        //then


    }
}