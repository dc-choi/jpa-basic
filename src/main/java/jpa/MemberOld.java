package jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 엔티티 매핑
 *
 * 객체와 테이블 매핑: @Entity, @Table
 * 필드와 컬럼 매핑: @Column
 * 기본 키 매핑: @Id
 * 연관관계 매핑: @ManyToOne, @JoinColumn
 *
 * @Entity
 * @Entity가 붙은 클래스는 JPA가 관리하게 된다.
 * JPA를 사용해서 테이블과 매핑할 클래스는 @Entity가 필수
 *
 * 주의점
 * 기본 생성자가 필수다. (파라미터가 없는 public 또는 protected 생성자)
 * final 클래스, enum, interface, inner 클래스 사용 X
 * 저장할 필드에 final 사용 X
 *
 * @Table
 * @Table은 엔티티와 매핑할 테이블 지정
 *
 * uniqueConstraints 속성을 사용하면 유니크키에 대한 제약조건을 걸을 수 있다.
 * @UniqueConstraint 어노테이션을 사용해서 정의한다.
 * columnNames라는 속성을 통해 컬럼을 지정한다. 컬럼들은 ,로 구분한다.
 *
 * indexes 속성을 사용하면 인덱스를 정의할 수 있다.
 * @Index 어노테이션을 사용해 정의하고 다음 속성을 사용한다.
 * 1. columnList: 인덱스를 구성하는 컬럼을 지정한다. 컬럼들은 ,로 구분된다. (필수)
 * 2. name: 인덱스의 이름을 지정한다. (선택)
 * 3. unique: 해당 인덱스가 유니크키인지 여부를 결정한다. (선택, 기본값을 false)
 *
 * @Column
 * 컬럼 매핑
 *
 * 속성
 * 1. name: 필드와 매핑할 테이블의 컬럼 이름
 * 2. insertable, updatable: 등록, 변경 가능 여부
 * 3. nullable: null의 허용 여부, false로 설정하면 not null로 설정된다.
 * 4. unique: @Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제약조건을 걸 때 사용 (실무에서는 @Table에 건다. 랜덤 이름이 부여된다.)
 * 5. columnDefinition: 데이터베이스 컬럼 정보를 직접 줄 수 있다.
 * 6. length: 문자 길이 제약조건, String 타입에만 사용한다.
 * 7. precision, scale: BigDecimal, BigInteger에서만 사용 가능하다. precision은 소수점을 포함한 전체 자리수, scale은 소수의 자리수이다.
 *
 * @Temporal
 * 날짜 타입 매핑
 * 참고: LocalDate, LocalDateTime을 사용할 때는 생략 가능(최신 하이버네이트 지원)
 *
 * @Enumerated
 * enum 타입 매핑
 *
 * 속성
 * EnumType.ORDINAL: enum 순서를 데이터베이스에 저장 (이건 절대 사용 X, enum에 순서를 임의로 작성하면 뒤틀림)
 * EnumType.STRING: enum 이름을 데이터베이스에 저장
 *
 * @Lob
 * BLOB, CLOB 매핑
 * 매핑하는 필드타입이 문자면 CLOB, 나머지는 BLOB 매핑
 * CLOB: String, char[], java.sql.CLOB
 * BLOB: byte[], java.sql.BLOB
 *
 * @Id
 * PK인지 알려주는 어노테이션
 *
 * @GeneratedValue
 * PK 자동생성을 도와주는 어노테이션
 *
 * 속성
 * IDENTITY: 데이터베이스에 위임, MYSQL에서 주로 사용한다.
 * JPA는 보통 트랜잭션 커밋 시점에서 SQL 사용
 * 데이터베이스에 기본키 생성을 위임하면 데이터베이스에 SQL을 질의한 후 ID값을 알 수 있음
 * IDENTITY 전략은 persist() 시점에 즉시 SQL을 실행하고 DB에서 식별자를 조회함.
 * 그래서 IDENTITY 전략을 사용하는 엔티티는 생성시점에서 바로 식별자를 사용할 수 있음.
 * 성능상 좀 느릴 수 있으나 그렇게 크게 와닿지는 않는다고 한다.
 *
 * SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용, ORACLE (@SequenceGenerator 필요)
 * IDENTITY와 다르게 시퀀스만 질의해서 생성한 후 트랜잭션 커밋 시점에서 SQL을 사용한다.
 * SEQUENCE 전략을 사용하는 엔티티도 생성시점에서 바로 식별자를 사용할 수 있음.
 * 성능상 한번에 SQL을 질의하기 때문에 성능이 더 좋을 수 있다.
 *
 * TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용 (@TableGenerator 필요)
 * AUTO: DB에 따라 자동 지정, 기본값
 *
 * 권장하는 식별자 전략
 * 기본키 제약 조건: null X, 유일해야함, 변하면 안된다.
 * 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대체키를 사용하자.
 *
 * 권장: Long + 대체키 + 키 생성전략 사용
 */
@Entity
@Table(name = "member_old", uniqueConstraints = @UniqueConstraint(name = "name", columnNames = "name"))
//@SequenceGenerator(
//        name = "MEMBER_SEQ_GENERATOR", // 식별자 생성기 이름
//        sequenceName = "MEMBER_SEQ", // 매핑할 데이터베이스 시퀀스 이름
//        initialValue = 1, // DDL 생성시에만 사용됨
//        allocationSize = 1
//        // 시퀀스 한번 호출에 증가하는 수, 성능을 향상시킬때 사용한다고 함. 한번에 50까지 불러올 수 있는 옵션이라고 함. 동시성 이슈도 없다고 함...
//        // 단, 데이터베이스 시퀀스 값이 하나씩 증가하도록 설정되어 있으면 이 값은 반드시 1로 설정해야한다.
//)
@Getter
@Setter
public class MemberOld {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스를 사용할 경우 추가, generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "roleType")
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(name = "createdDate", columnDefinition = "DATETIME")
    private LocalDateTime createdDate;

    @Column(name = "lastModifiedDate", columnDefinition = "DATETIME")
    private LocalDateTime lastModifiedDate;

    @Lob
    @Column(name = "description")
    private String description;

    @Transient // 특정 프로퍼티를 컬럼에 매핑하지 않음
    private String temp;
}
