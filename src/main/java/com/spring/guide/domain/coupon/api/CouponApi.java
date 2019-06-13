package com.spring.guide.domain.coupon.api;

import com.spring.guide.domain.coupon.application.CouponUseService;
import com.spring.guide.domain.coupon.dao.CouponHelperService;
import com.spring.guide.domain.coupon.domain.CouponCode;
import com.spring.guide.domain.coupon.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponApi {


    private final CouponHelperService couponHelperService;
    private final CouponUseService couponUseService;

    @GetMapping("/{code}")
    public CouponResponse getCoupon(@PathVariable final String code) {
        return new CouponResponse(couponHelperService.findByCode(CouponCode.of(code)));
    }

    @PutMapping("/{code}")
    public void useCoupon(@PathVariable final String code) {
        couponUseService.use(CouponCode.of(code));
    }

}
