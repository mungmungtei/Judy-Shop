package judyshop.shoppingmall.repository;

import judyshop.shoppingmall.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        // id 값이 없으면 DB에 아직 저장된 적이 없는 새로운 엔티티로 판단 -> 영속화(persist) 하기
        if (item.getId() == null) {
            em.persist(item);
        // id 값이 있으면 한번이라도 영속화 되었지만 지금은 준영속 상태라는 의미
        // merge 호출 시, 준영속 엔티티의 식별자 값으로 영속 엔티티를 조회한 후, 영속 엔티티 값을 준영속 엔티티 값으로 모두 교체함! (병합)
        // 트랜잭션 커밋 시점에 변경 감지 기능이 동작해서 DB에 update 쿼리 보내짐
        // 이때, 값이 없는 것은 null로 업데이트 될 수도 있어 위험하다. 그래서 가급적 merge 사용 X !!
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 단건 조회는 find 쓰면 되지만, 여러개 찾는 것은 JPQL 작성해야 함
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
