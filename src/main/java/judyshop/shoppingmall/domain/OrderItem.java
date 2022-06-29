package judyshop.shoppingmall.domain;

import judyshop.shoppingmall.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 주문 상품

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 주문

    private int orderPrice; // 주문 당시 가격
    private int count; // 주문 당시 수량

    // == 생성 메서드 ==
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);  // 주문하면 재고 수량에서 주문 수량 감소시켜줘야 함
        return orderItem;
    }

    // == 비즈니스 로직 ==
    public void cancel() {  // 주문 취소
        getItem().addStock(count);  // 주문 취소했으니, 주문했던 수량만큼 재고 수량을 증가시켜줘야 함
    }

    // == 조회 로직 ==
    public int getTotalPrice() {  // 전체 주문 가격 조회
        return getOrderPrice() * getCount();
    }

}
