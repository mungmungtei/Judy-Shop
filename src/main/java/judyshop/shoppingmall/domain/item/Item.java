package judyshop.shoppingmall.domain.item;

import judyshop.shoppingmall.domain.Category;
import judyshop.shoppingmall.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 부모 클래스에 상속 관계 전략 작성하기
@DiscriminatorColumn(name = "dtype") // 싱글 페이지 안에서 클래스 구분해주기
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    // == 비즈니스 로직 ==  <- 데이터(여기서는 stockQuantity)를 가지고 있는 쪽에 비즈니스 로직이 있는게 응집력이 있어 관리하기 좋음
    public void addStock(int quantity) {  // 재고 수량 증가
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {  // 재고 수량 감소
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고 확인이 필요합니다!");
        }
        this.stockQuantity = restStock;
    }

}
