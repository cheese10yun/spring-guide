package com.spring.guide.domain.coupon.dao;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.spring.guide.domain.coupon.CouponBuilder;
import com.spring.guide.domain.coupon.domain.Coupon;
import com.spring.guide.domain.coupon.domain.CouponCode;
import com.spring.guide.domain.coupon.exception.CouponNotFoundException;
import com.spring.guide.test.MockTest;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


public class CouponFindDaoTest extends MockTest {

    private Coupon coupon;

    @InjectMocks
    private CouponFindDao couponFindDao;

    @Mock
    private CouponRepository couponRepository;

    @Before
    public void setUp() throws Exception {
        coupon = CouponBuilder.build();
    }

    @Test
    public void findByCode_존재하는경우() {
        //given
        given(couponRepository.findByCode(any())).willReturn(Optional.of(coupon));

        //when
        final CouponCode code = CouponCode.generateCode();
        final Coupon coupon = couponFindDao.findByCode(code);

        //then
        assertThat(coupon).isNotNull();
    }

    @Test(expected = CouponNotFoundException.class)
    public void findByCode_없는경우() {
        //given
        given(couponRepository.findByCode(any())).willReturn(Optional.empty());

        //when
        final CouponCode code = CouponCode.generateCode();
        couponFindDao.findByCode(code);

        //then
    }
}