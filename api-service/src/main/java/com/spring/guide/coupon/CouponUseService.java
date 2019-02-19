package com.spring.guide.coupon;

import com.spring.guide.domain.coupon.Coupon;
import com.spring.guide.domain.coupon.CouponCode;
import com.spring.guide.domain.coupon.CouponHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponUseService {

    private final CouponHelperService couponHelperService;

    public void use(final CouponCode code) {
        final Coupon coupon = couponHelperService.findByCode(code);
        coupon.use();
    }
}
