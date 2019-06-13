package com.spring.guide.domain.coupon.application;

import com.spring.guide.domain.coupon.dao.CouponFindDao;
import com.spring.guide.domain.coupon.domain.Coupon;
import com.spring.guide.domain.coupon.domain.CouponCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponUseService {

  private final CouponFindDao couponFindDao;

    public void use(final CouponCode code) {
      final Coupon coupon = couponFindDao.findByCode(code);
        coupon.use();
    }
}
