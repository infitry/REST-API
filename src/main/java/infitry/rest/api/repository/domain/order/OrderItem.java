package infitry.rest.api.repository.domain.order;

import infitry.rest.api.repository.domain.item.Item;
import infitry.rest.api.repository.domain.order.code.OrderItemStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity(name = "tb_order_item")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @Column(nullable = false)
    int orderPrice;

    @Column(nullable = false)
    int count;

    @Column
    LocalDateTime cancelDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderItemStatus orderItemStatus;

    public void addOrder(Order order) {
        this.order = order;
    }

    private OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
        this.orderItemStatus = OrderItemStatus.NORMAL;
    }

    //== 생성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        item.removeStock(count);    // 재고감소
        return new OrderItem(item, orderPrice, count);
    }

    //== 비즈니스 로직 ==//
    /**
     * * 부분 취소
     */
    public void cancel() {
        item.addStock(count);   // 재고 복구
        this.orderItemStatus = OrderItemStatus.CANCEL;
        this.cancelDate = LocalDateTime.now();
        boolean isAllCancel = order.getOrderItems().stream()
                .noneMatch(orderItem -> OrderItemStatus.NORMAL == orderItem.getOrderItemStatus());
        if (isAllCancel) {  // 전체 상품 취소 시 주문도 취소
            order.cancelExcludeOrderItem();
        }
    }

    /**
     * * 총 상품가격 (수량 * 주문당시 상품가격)
     */
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}
