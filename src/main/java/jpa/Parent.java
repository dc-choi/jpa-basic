package jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // 영속성 전이가 되면서 고아객체를 관리한다.
    // 스스로 생명주기를 관리하는 persist()와 remove로 제거할 수 있다.
    // 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음.
    // 도메인 주도 설계의 Aggregate Root 개념을 구현할 때 유용함.
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList;

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }
}
