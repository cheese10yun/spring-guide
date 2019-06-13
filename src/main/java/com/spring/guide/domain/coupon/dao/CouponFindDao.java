package com.spring.guide.domain.coupon.dao;

import com.spring.guide.domain.coupon.domain.Coupon;
import com.spring.guide.domain.coupon.domain.CouponCode;
import com.spring.guide.domain.coupon.exception.CouponNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponFindDao {

    private final CouponRepository couponRepository;

    public Coupon findByCode(final CouponCode code) {
        final Optional<Coupon> coupon = couponRepository.findByCode(code);
        coupon.orElseThrow(() -> new CouponNotFoundException(code.getValue()));
        return coupon.get();
    }

}
