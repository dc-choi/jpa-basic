package jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 상속관계 매핑
 *
 * 관계형 데이터베이스는 상속 관계가 없다.
 * 슈퍼타입 서브타입 관계라는 모델링 기법이 객체의 상속과 유사하다.
 * 그래서 JPA에서는 상속관계를 매핑할 때 객체의 상속과 DB의 슈퍼타입 서프타입 관계를 매핑한다.
 *
 * 슈퍼타입 서브타입 논리 모델을 실제 물리 모델로 구현하는 방법은 다음과 같다.
 * 1. 각각 테이블로 변환 -> 조인 전략 (InheritanceType.JOINED)
 * 테이블이 정규화가 되어있고 외래키 참조 무결성 제약조건을 활용 가능히다. 저장공간의 효율도 챙길 수 있다.
 * 조회시 조인을 많이 사용하여 성능이 저하되고 조회 쿼리가 복잡해진다. 데이터 저장시 INSERT SQL을 2번 호출한다.
 *
 * 2. 통합 테이블로 변환 -> 단일 테이블 전략 (InheritanceType.SINGLE_TABLE)
 * 조인이 필요가 없어서 일반적으로 조회 성능이 빠르고 조회 쿼리가 단순함.
 * 자식 엔티티가 매핑한 컬럼은 모두 null을 허용하고 단일 테이블에 모든 걸 저장하여 테이블이 커질 수 있다.
 * 상황에 따라 조회 성능이 오히려 느려질 수 있다.
 *
 * 3. 서브타입 테이블로 변환 -> 구현 클래스마다 테이블 전략 (InheritanceType.TABLE_PER_CLASS)
 * 이 전략은 DBA와 개발자 둘 다 메리트가 없는 전략이다...
 * UNION SQL로 여러 자식 테이블을 함께 조회하기 때문에 성능이 느리다...
 * 자식 테이블을 통합해서 쿼리하기 어려움...
 *
 * JPA의 기본 전략은 단일 테이블 전략이다.
 */
@Entity
@Table(name = "item_map")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 20) // 상속관계 매핑을 할 때 구분자 컬럼을 지정해주는 어노테이션
@Getter
@Setter
public class Item extends BaseEntity {
    private String name;
}
