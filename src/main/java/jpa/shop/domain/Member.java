package jpa.shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 영한님도 가급적이면 테이블에 대한 명세를 자세히 한다고 함.
 */
@Entity
@Table(name = "member")
@Getter
@Setter // 실무에서는 엔티티가 어디서든 바뀔 수 있어서 잘 사용하지 않는다. (생성자로 하는거 추천)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String city;

    private String street;

    private String zipcode;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orders;
}
