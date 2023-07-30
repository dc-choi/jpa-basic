package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

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
            MemberOld member = new MemberOld();
            member.setName("choi dong chul");
            entityManager.persist(member); // create

            MemberOld findMember = entityManager.find(MemberOld.class, 1L); // read
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            findMember.setName("dong chul"); // update

            // JPQL을 사용함. 쿼리시 JPA 객체를 사용해서 질의를 진행함.
            List<MemberOld> findMembers = entityManager.createQuery("select m from MemberOld m")
                    .setFirstResult(1) // offset
                    .setMaxResults(100) // limit
                    .getResultList();

            Team team = new Team();
            team.setName("A");
            entityManager.persist(team);

            Developer developer = new Developer();
            developer.setName("choi");
            developer.setTeam(team); // 이 코드만 있어도 데이터의 누락은 없다.
            entityManager.persist(developer);

            // team.getDeveloper().add(developer); // 하지만 양방향 매핑관계에서는 이 코드도 있는게 좋음.

            // entityManager.flush(); // 변경내용 동기화
            // entityManager.clear(); // 영속성 컨텍스트 초기화

//            Developer findDeveloper = entityManager.find(Developer.class, developer.getId());
//
//            Team findTeam = findDeveloper.getTeam();
//            System.out.println("findDeveloper = " + findDeveloper.getName());
//            System.out.println("findTeam = " + findTeam.getName());

            // Developer findDev = entityManager.find(Developer.class, developer.getId());
            Team findTeam = entityManager.find(Team.class, team.getId()); // 1차 캐시에 있어서 flush를 안하면 select가 안됨
            List<Developer> findDev = findTeam.getDeveloper();
            for (Developer developer1 : findDev) {
                System.out.println("developer1 = " + developer1.getName());
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        entityManager.close();
        entityManagerFactory.close();
    }
}
