package jpa;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @MappedSuperclass
 *
 * 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
 * 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통 으로 적용하는 정보를 모을 때 사용
 *
 * 상속관계 매핑이 아니고 엔티티와 테이블에도 매핑되지 않는다.
 * 부모 클래스를 상속받는 자식 클래스에 매핑 정보만 제공
 * 조회, 검색 불가(entityManager.find(BaseEntity) 불가능)
 * 직접 생성해저 사용할 일이 없으므로 추상 클래스로 만드는 것을 권장한다.
 *
 * 참고: @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속 가능
 */
@MappedSuperclass
// @EntityListeners(AuditingEntityListener.class) // 엔티티의 라이프사이클 이벤트를 감지하는 리스너를 지정한다.
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @CreatedDate // @EntityListeners와 같이 사용됨.
    @Column(name = "created_at", columnDefinition = "DATETIME", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @LastModifiedDate // @EntityListeners와 같이 사용됨.
    @Column(name = "updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;
}
