package com.spring.guide.domain.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(CouponCode code);

    boolean existsByCode(CouponCode code);

}
