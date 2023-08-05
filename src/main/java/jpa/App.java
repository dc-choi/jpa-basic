package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;

/**
 * 영속성 컨텍스트
 * 엔티티를 영구 저장하는 환경이라는 뜻이다.
 * 영속성 컨텍스트는 논리적인 개념이다. DB에 실제로 저장되는게 아니고 이 컨텍스트에 저장이 된다고 한다.
 * 엔티티 매니저를 통해 영속성 컨텍스트에 접근한다.
 *
 * 엔티티의 생명주기
 * 비영속 (new/transient)
 * 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
 * 엔티티 객체를 아예 새로 만든 상태이다.
 *
 * 영속 (managed)
 * 영속성 컨텍스트에 관리되는 상태
 * persist() 메서드를 통해 영속성 컨텍스트에 저장된다.
 * 트랜잭션이 커밋되는 시점에서 영속성 컨텍스트의 데이터가 DB에 SQL로 질의됨.
 * flush() 메서드를 통해 영속성 컨텍스트의 변경내용을 데이터베이스에 반영하기도 한다.
 *
 * 영속성 컨텍스트의 장점
 * 1. 1차 캐시
 * 조회를 하는 경우 먼저 캐시된 영속성 컨텍스트에서 찾고 없으면 DB에 접근한다.
 * DB에 접근해서 데이터를 가져오면 영속성 컨텍스트에 저장하게 되고 데이터를 반환한다.
 * 1차 캐시의 경우 한 트랜잭션 안에서 공유되는 캐시이며 어플리케이션 전체에서 공유하는 캐시는 아니다. 그건 2차 캐시라고 부른다.
 * 복잡한 비즈니스 로직을 작성하는 경우 DB 접근을 줄일 수 있는 장점이 있다.
 *
 * 2. 동일성 보장
 * JAVA 컬렉션에서 데이터를 꺼내오면 참조값이 같아서 == 비교를 했을 때 true가 나온다.
 * REPEATABLE READ 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공
 * REPEATABLE READ: 선행 트랜잭션이 읽은 데이터는 트랜잭션이 종료될 때까지 후행 트랜잭션이 갱신하거나 삭제하는 것은 불허함으로써 같은 데이터를 두 번 쿼리했을 때 일관성 있는 결과를 리턴
 *
 * 3. 트랜잭션을 지원하는 쓰기 지연
 * persist()를 하게되면 1차 캐시와 쓰기 지연 SQL 저장소에 저장된다.
 * 트랜잭션을 커밋하는 시점에서 INSERT SQL을 보내기 때문에 그전까지 INSERT SQL을 DB에 저장하지 않는다.
 *
 * 4. 변경 감지
 * JAVA 컬렉션을 사용하는 것처럼 update() 메서드를 사용하지 않아도 값이 변경되어도 DB에 자동으로 저장됨.
 * 값을 최초로 읽어둔 시점의 스냅샷을 영속성 컨텍스트에 저장하게 되고 그 스냅샷을 변경된 엔티티랑 비교하게 된다.
 * 비교해서 변경된 부분을 쓰기 지연 SQL 저장소에 저장하게 된다.
 * 트랜잭션이 커밋되는 시점에서 변경사항에 대한 SQL문을 질의하게 된다.
 *
 * 참고: flush가 발생하면?
 * 1. 변경감지가 일어나게 된다.
 * 2. 수정된 엔티티 쓰기 지연 SQL 저장소에 등록한다.
 * 3. 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송
 *
 * 참고: 영속성 컨텍스트를 flush 하는 경우
 * 1. 직접 호출
 * 2. 트랜잭션 커밋
 * 3. JPQL 쿼리 실행
 *
 * 참고: flush는 영속성 컨텍스트를 비우지않는다.
 * 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화한다.
 * 트랜잭션이라는 작업 단위가 중요하고 커밋 직전에만 동기화 하면 된다.
 *
 * 5. 지연 로딩
 * 지연로딩에 대해서 알기 전에 프록시에 대해서 알아야 함.
 * 지연로딩을 사용할 때 프록시를 사용하기 때문이다.
 *
 * 프록시
 * entityManager.find() VS entityManager.getReference()
 * entityManager.find()는 DB를 통해 실제 엔티티 객체를 조회
 * entityManager.getReference()는 DB 조회를 미루는 가짜 프록시 엔티티 객체 조회
 *
 * 프록시 특징
 * 실제 클래스를 상속 받아서 만들어졌다. 실제 클래스와 겉 모양이 똑같다.
 * 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨. (이론상)
 * 처음 사용할 때 한 번만 초기화한다. 초기화 할 때 프록시 객체가 실제 엔티티로 바뀌는게 아님.
 * 초기화 되면 프록시 객체를 통해서 실제 엔티티에 접근할 수 있는 것이다.
 * 프록시 객체는 원본 엔티티를 상속받음. 따라서 타입 체크시 주의해야 함. (== 비교는 실패하고 instanceof를 사용해야 함.)
 * 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 entityManager.getReference()를 호출해도 실제 엔티티를 반환함.
 * 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
 *
 * 즉시 로딩 VS 지연 로딩
 * 가급적 지연 로딩만 사용(특히 실무에서)
 * 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생한다.
 * 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.
 * 모든 연관 관계에서 지연 로딩을 사용해라! 실무에서 즉시 로딩을 사용하지마라...
 * JPQL fetch 조인이나, 엔티티 그래프 기능을 사용해라!
 *
 * 준영속 (detached)
 * 영속성 컨텍스트에 저장되었다가 분리된 상태
 * detach() 메서드를 통해 영속성 컨텍스트에서 분리한다.
 * 영속성 컨텍스트가 제공하는 기능을 사용하지 못한다.
 *
 * 준영속 상태로 만드는 방법
 * 1. detach()
 * 특정 엔티티만 준영속 상태로 전환
 *
 * 2. clear()
 * 영속성 컨텍스트를 완전 초기화
 *
 * 3. close()
 * 영속성 컨텍스트를 종료
 *
 * 삭제 (removed)
 * 삭제된 상태
 * remove() 메서드를 통해 객체를 삭제한다.
 *
 * 영속성 전이: CASCADE
 * 특정 엔티티를 영속 상태를 만들 때 연관된 엔티티도 함께 영속상태로 만들고 싶을 때 사용
 * EX) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장.
 * 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
 * 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐
 * 보통 ALL, PERSIST, REMOVE만 사용한다고 한다.
 *
 * 고아 객체
 * 고아 객체를 제거한다하면 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 것을 말한다.
 * 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능이다.
 * 참조하는 곳이 하나일 때 사용해야하고 특정 엔티티가 개인 소유할 때 사용한다.
 * 즉, OneToOne OneToMany일 경우에만 사용 가능하다.
 * orphanRemoval 옵션을 true하면 사용할 수 있다.
 */
public class App {
    public static void main(String[] args) {
        // 설정정보를 가져와서 EntityManagerFactory를 만듬. EntityManagerFactory는 EntityManager를 관리해주는 객체이다.
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        // EntityManagerFactory에서 EntityManager를 가져옴. EntityManager는 풀에서 커넥션을 하나 가져온다.
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행된다.
        try {
//            MemberOld member = new MemberOld();
//            member.setName("choi dong chul");
//            entityManager.persist(member); // create
//
//            MemberOld findMember = entityManager.find(MemberOld.class, 1L); // read
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getName() = " + findMember.getName());
//
//            findMember.setName("dong chul"); // update
//
//            // JPQL을 사용함. 쿼리시 JPA 객체를 사용해서 질의를 진행함.
//            List<MemberOld> findMembers = entityManager.createQuery("select m from MemberOld m")
//                    .setFirstResult(1) // offset
//                    .setMaxResults(100) // limit
//                    .getResultList();
//
//            Team team = new Team();
//            team.setName("A");
//            entityManager.persist(team);
//
//            Developer developer = new Developer();
//            developer.setName("choi");
//            developer.setTeam(team); // 이 코드만 있어도 데이터의 누락은 없다.
//            entityManager.persist(developer);
//
//            // team.getDeveloper().add(developer); // 하지만 양방향 매핑관계에서는 이 코드도 있는게 좋음.
//
//            // entityManager.flush(); // 변경내용 동기화
//            // entityManager.clear(); // 영속성 컨텍스트 초기화
//
////            Developer findDeveloper = entityManager.find(Developer.class, developer.getId());
////
////            Team findTeam = findDeveloper.getTeam();
////            System.out.println("findDeveloper = " + findDeveloper.getName());
////            System.out.println("findTeam = " + findTeam.getName());
//
//            // Developer findDev = entityManager.find(Developer.class, developer.getId());
//            Team findTeam = entityManager.find(Team.class, team.getId()); // 1차 캐시에 있어서 flush를 안하면 select가 안됨
//            List<Developer> findDev = findTeam.getDeveloper();
//            for (Developer developer1 : findDev) {
//                System.out.println("developer1 = " + developer1.getName());
//            }

            Address address = Address.builder()
                    .city("a")
                    .street("b")
                    .zipcode("c")
                    .build();

            MemberOld memberOld = MemberOld.builder()
                    .address(address)
                    .build();

            System.out.println("memberOld.getAddress() = " + memberOld.getAddress());
            System.out.println("memberOld.getAddress().getCity() = " + memberOld.getAddress().getCity());

            Child child = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child);
            parent.addChild(child2);

            entityManager.persist(parent);

            entityManager.flush();

            Parent findParent = entityManager.find(Parent.class, 1L);
            System.out.println("findParent.getName = " + findParent.getName());
            System.out.println("findParent.getChilds = " + findParent.getChildList());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        entityManager.close();
        entityManagerFactory.close();
    }
}
