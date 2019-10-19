package com.spring.guide.domain.order.domain;

import com.spring.guide.domain.product.domain.ItemSize;
import com.spring.guide.domain.product.domain.ProductItem;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private ItemSize size;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public OrderItem(ProductItem productItem) {
        this.name = productItem.getName();
        this.size = productItem.getSize();
    }
}
