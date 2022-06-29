package judyshop.shoppingmall.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 직접 생성하면 안되고, 생성 스타일이 정해져 있음을 알려주기
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 모든 연관관계는 지연로딩으로 설정하기!!
    // ~ToOne 으로 끝나면 FetchType.LAZY 로 잡아주기! (EAGER 가 디폴트) / ~ToMany 는 LAZY 가 디폴트라서 따로 설정할 필요 없음
    // 즉시로딩이란 하나를 조회할 때 연관된 모든걸 다 한번에 조회하는 것. 하나를 로딩하는 시점에 다른 것들도 다 끌어와서 로딩함. (N+1 문제)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래키 member_id 로 주문한 회원 정보를 매핑
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // cascade : persist를 한번에 해주는 기능 (타입 ALL로 해서 delete도 한번에)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송 정보

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING) // EnumType 디폴트는 ORDINAL(숫자)인데, 반드시 STRING 으로 하기! 그래야 중간에 값이 들어가도 순서 안밀림
    private OrderStatus status; // 주문 상태 [ ORDER, CANCEL ]

    // == 연관관계 메서드 ==
    // 연관관계 주인을 떠나 양방향 연관관계에서는 값을 양쪽에 다 넣어줘야 하니깐 양쪽 모두 값을 셋팅해줘야 함.
    // DB에 저장하는건 연관관계 주인 테이블에만 있으면 되는데 로직을 태울때 왔다 갔다 하려면 양쪽에 있어야 함.
    // 그래서 연관관계 메서드를 사용해서 양방향에 다 걸리게 만드는 것.
    // 핵심적으로 컨트롤 하는 쪽에 있는게 연관관계 메서드를 작성하는게 좋음.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 ==
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // == 비즈니스 로직 ==
    public void cancel() {  // 주문 취소
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        // 주문 취소 시, 주문했던 상품들 각각 다 취소해줘야 함
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // == 조회 로직 ==
    public int getTotalPrice() {  // 전체 주문 가격 조회
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
