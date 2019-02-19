package com.spring.guide.coupon;

import com.spring.guide.domain.coupon.CouponCode;
import com.spring.guide.domain.coupon.CouponHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
