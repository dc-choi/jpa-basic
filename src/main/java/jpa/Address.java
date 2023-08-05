package jpa;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 값 타입의 비교
 *
 * 동일성 비교: 인스턴스의 참조값을 비교, == 사용
 * 동등성 비교: 인스턴스의 값을 비교, equals() 사용
 * 값 타입은 equals()를 사용해서 동등성 비교를 해야 함.
 * 값 타입의 equals() 메서드를 적절하게 재정의 (주로 모든 필드 사용)
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Column(name = "city", columnDefinition = "VARCHAR(20)")
    private String city;
    @Column(name = "street", columnDefinition = "VARCHAR(20)")
    private String street;
    @Column(name = "zipcode", columnDefinition = "VARCHAR(20)")
    private String zipcode;

    @Builder
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
