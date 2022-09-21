package infitry.rest.api.repository.domain.order;

import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.domain.common.BaseTimeEntity;
import infitry.rest.api.repository.domain.order.code.OrderItemStatus;
import infitry.rest.api.repository.domain.order.code.OrderStatus;
import infitry.rest.api.repository.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Entity(name = "tb_order")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    LocalDateTime orderDate;

    @Column
    LocalDateTime cancelDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    OrderStatus orderStatus;

    private Order(User user, OrderItem... orderItems) {
        this.user = user;
        Arrays.stream(orderItems).forEach(this::addOrderItem);
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.ORDER;
    }

    //== 연관관계 메서드 ==//
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.addOrder(this);
    }

    public void setUser(User user) {
        this.user = user;
    }

    //== 생성 메서드 ==//
    public static Order createOrder(User user, OrderItem... orderItems) {
        return new Order(user, orderItems);
    }

    //== 비즈니스 로직 ==//
    /**
     * * 주문 취소 (주문상품 포함)
     */
    public void cancel() {
        if (this.orderStatus == OrderStatus.DELIVERY_COMPLETED) {
            throw new ServiceException("이미 배송 된 상품은 취소가 불가능 합니다.");
        }
        this.orderStatus = OrderStatus.CANCEL;
        this.cancelDate = LocalDateTime.now();
        this.orderItems.forEach(OrderItem::cancel);
    }

    /**
     * 주문 취소 (주문상품 미포함) *
     */
    public void cancelExcludeOrderItem() {
        if (this.orderStatus == OrderStatus.DELIVERY_COMPLETED) {
            throw new ServiceException("이미 배송 된 상품은 취소가 불가능 합니다.");
        }
        this.orderStatus = OrderStatus.CANCEL;
        this.cancelDate = LocalDateTime.now();
    }

    /**
     * * 전체 주문가격 조회
     */
    public int getTotalPrice() {
        return this.orderItems.stream()
                .filter(orderItem -> orderItem.getOrderItemStatus() == OrderItemStatus.NORMAL)
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    /**
     * * 배송완료 처리
     */
    public void setDeliveryCompletedStatus() {
        this.orderStatus = OrderStatus.DELIVERY_COMPLETED;
    }
}
