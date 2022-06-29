package judyshop.shoppingmall.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // DB id 컬럼명 변경해주기
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // Order 테이블에 있는 member 필드에 의해서 매핑된 거울(읽기 전용)
    private List<Order> orders = new ArrayList<>();
}
