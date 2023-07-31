package jpa.shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private Integer stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItem;

    // mappedBy가 있는 쪽이 주인이 아니다.
    @ManyToMany(mappedBy = "items")
    private List<Category> categories;
}
