package jpa;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * Embedded 타입
 *
 * 중복된 값을 한번에 정의하기에 좋음. 기본 생성자가 필수다.
 * Embedded 타입은 엔티티의 값을 뿐이다.
 * Embedded 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
 * 객체와 테이블을 아주 세밀하게 매핑하는 것이 가능하다.
 * 잘 설계한 ORM은 매핑한 테이블의 수보다 클래스의 수가 더 많음
 *
 * 장점
 * 1. 재사용성
 * 2. 높은 응집도
 * 3. 해당 값 타입만 사용하는 의미있는 메서드를 만들 수 있음.
 * 4. Embedded 타입을 포함한 모든 값 타입은 값 타입을 소유한 엔티티에 생명주기를 의존함
 *
 * 값 타입 공유 참조
 * Embedded 타입을 여러 엔티티에서 공유하면 위험함
 * 같은 참조값을 사용하는 side effect가 발생할 수 있음.
 * 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입이다.
 * 기본 타입에 값을 대입하면 값을 복사한다.
 * 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다.
 * 객체의 공유 참조는 피할 수 없음.
 *
 * 불변 객체
 *
 * 생성 시점 이후로 절대 값을 변경할 수 없는 객체
 * 객체 타입을 수정할 수 없게 만들면 공유 참조를 막을 수 있다.
 * 값 타입은 불변 객체로 설계해야함.
 * 생성자로만 값을 설정하고 setter를 만들지 않으면 됨.
 */
@Embeddable // 값을 내장하는 타입, 객체지향적으로 사용할 수 있다.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {
    @Column(name = "started_at", columnDefinition = "DATETIME")
    private LocalDateTime startDate;

    @Column(name = "ended_at", columnDefinition = "DATETIME")
    private LocalDateTime endDate;
}
