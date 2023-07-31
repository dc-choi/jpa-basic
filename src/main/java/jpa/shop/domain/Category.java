package jpa.shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child;

    // @JoinTable이 있는 쪽이 주인에 해당함.
    @ManyToMany
    @JoinTable(
            name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"), // 현 엔티티 입장에서 조인시키는 것
            inverseJoinColumns = @JoinColumn(name = "item_id") // 반대편 엔티티 입장에서 조인시키는 것
    )
    private List<Item> items;
}
