package com.spring.guide.domain.coupon;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(CouponCode code);

    boolean existsByCode(CouponCode code);

}
