package jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "team")
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    /**
     * 1의 입장에서 N에 매핑시킬 때 사용하는 어노테이션이며 기본적으로 지연로딩을 지원한다.
     * mappedBy의 경우 쉽게 말하면 N 입장에서 매핑되는 프로퍼티명에 매핑을 시키는 것이다.
     * 이는 객체와 테이블이 관계를 맺는 차이에서 비롯된 것이다.
     * 객체의 양방향 관계는 사실 양방향 관계가 아니라 서로 다른 단방향 관계 2개다.
     * DB의 테이블처럼 완전한 양방향 관계가 아니기 때문에 연관관계의 주인을 설정해야한다.
     * 연관관계의 주인만이 외래키를 관리한다. 이 말은 주인만이 등록, 수정을 할 수 있고 주인이 아니면 읽기만 가능하다.
     * 주인은 mappedBy 속성을 사용하지 않는다. 주인이 아니면 mappedBy로 주인을 지정한다.
     * 일반적으로 외래키가 있는 곳을 주인으로 정한다.
     */
    @OneToMany(mappedBy = "team")
    private List<Developer> developer;

    /**
     * 양방향 매핑시 가장 많이 하는 실수
     *
     * 1. 연관관계의 주인에 값을 입력하지 않음
     * 즉, N의 입장에서 1의 값을 세팅해야 데이터가 들어간다.
     * 순수한 객체 관계를 고려하면 가장 좋은 방법은 항상 양쪽 다 값을 입력해야 한다.
     * 그래서 결론은 연관관계를 맺어주는 편의 메서드를 생성하거나 주인이 있는쪽(N)에 1의 값을 입력한다.
     *
     * 2. 양방향 매핑시에 무한 루프를 조심해야 한다.
     * toString(), lombok, JSON 생성 라이브러리
     *
     * 결론적으로 단방향 매핑만으로도 이미 연관관계 매핑은 완료된다.
     * 양방향 매핑은 반대 방향으로 조회 기능(객체 그래프 탐색)이 추가 되는것 뿐
     * JPQL에선 역방향으로 탐색할 일이 많음
     * 단방향 매핑을 잘하고 양방향은 필요할 때 추가해도 됨. (테이블에 영향을 주지 않음)
     */
}
