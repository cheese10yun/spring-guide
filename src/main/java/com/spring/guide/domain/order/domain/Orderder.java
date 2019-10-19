package com.spring.guide.domain.order.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orderder {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "orderer_name", nullable = false)
    private String name;

    private Orderder(Long memberId, String name) {
        Assert.notNull(name, "name must be not null");
        this.memberId = memberId;
        this.name = name;
    }

    public static Orderder memberOrderer(Long memberId, String name) {
        Assert.notNull(memberId, "memberId must be not null");
        return new Orderder(memberId, name);
    }

    public static Orderder nonMemberOrderer(String name) {
        return new Orderder(null, name);
    }
}
