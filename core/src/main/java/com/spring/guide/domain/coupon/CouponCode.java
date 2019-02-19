package com.spring.guide.domain.coupon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.RandomString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCode {

    @NotEmpty
    @Column(name = "code", nullable = false, updatable = false, unique = true)
    private String value;

    private CouponCode(final String value) {
        this.value = value;
    }

    public static CouponCode generateCode() {
        return new CouponCode(RandomString.make(10));
    }

    public static CouponCode of(final String value) {
        return new CouponCode(value);
    }
}
