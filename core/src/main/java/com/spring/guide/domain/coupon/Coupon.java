package com.spring.guide.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.guide.domain.coupon.exception.CouponAlreadyUseException;
import com.spring.guide.domain.coupon.exception.CouponExpireException;
import com.spring.guide.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Embedded
    private CouponCode code;

    @Column(name = "used", nullable = false)
    private boolean used;

    @Column(name = "discount", nullable = false)
    private double discount;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDate expirationDate;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @Builder
    public Coupon(CouponCode code, double discount, LocalDate expirationDate, Member member) {
        this.code = code;
        this.discount = discount;
        this.expirationDate = expirationDate;
        this.member = member;
        this.used = false;
    }

    public boolean isExpiration() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public void use() {
        verifyExpiration();
        verifyUsed();
        this.used = true;
    }

    private void verifyUsed() {
        if (used) throw new CouponAlreadyUseException();
    }

    private void verifyExpiration() {
        if (LocalDate.now().isAfter(getExpirationDate())) throw new CouponExpireException();
    }


}
