package judyshop.shoppingmall.repository;

import judyshop.shoppingmall.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member); // persist : 영속성 컨텍스트에 넣어 놓고 나중에 트랜잭션이 커밋되는 순간에 DB에 반영
    }

    public Member findOne(Long id) { // 단건 조회
        return em.find(Member.class, id);
    }

    public List<Member> findAll() { // 전체 조회
        // SQL은 테이블을 대상으로 쿼리를 하고, JPQL은 엔티티 객체를 대상으로 쿼리를 함
       return em.createQuery("select m from Member m", Member.class)
               .getResultList();
    }

    public List<Member> findByName(String name) {  // 이름을 받아서 특정 회원 조회
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
