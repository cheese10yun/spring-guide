package com.spring.guide.domain.order.domain;

import com.spring.guide.domain.model.Address;
import com.spring.guide.domain.product.domain.ProductItem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Embedded
    private Orderder orderder;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "orders_item", joinColumns = @JoinColumn(name = "id", nullable = false))
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(Address address, Orderder orderder, List<ProductItem> productItems) {
        Assert.notNull(address, "address must not be null");
        Assert.notNull(orderder, "address must not be null");
        Assert.notEmpty(productItems, "address must not be empty");

        this.address = address;
        this.orderder = orderder;
        for (ProductItem productItem : productItems) {
            orderItems.add(new OrderItem(productItem));
        }
    }
}
