package judyshop.shoppingmall.domain;

import judyshop.shoppingmall.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "catetory_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",  // 테이블에서 다대다 관계를 풀기 위해서는 중간 매핑 테이블 필요함 (실무에서는 다대다 사용 X)
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns =  @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // == 연관관계 메서드 ==
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

}
