package jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 객체를 테이블에 맞추어 데이터 중심으로 모델링하면, 협력 관계를 민들 수 없다.
 * 테이블은 외래키로 조인을 사용해서 연관된 테이블을 찾는다.
 * 객체는 참조를 사용해서 연관된 객체를 찾는다.
 * 테이블과 객체의 패러다임은 이런 큰 간격이 있다.
 */
@Entity
@Table(name = "developer")
@Getter
@Setter
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // N 입장에서 1에 매핑시킬 때 사용하는 어노테이션
    // @ManyToOne의 경우 지연로딩으로 설정을 해주어야 한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") // id가 아니라 team_id로 해야 충돌이 일어나지 않는다.
    private Team team;

    @OneToOne
    @JoinColumn(name = "locker_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Locker locker;
}
