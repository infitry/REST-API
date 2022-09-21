package infitry.rest.api.repository.domain.order;


import infitry.rest.api.dto.user.AddressDto;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.repository.domain.item.Item;
import infitry.rest.api.repository.domain.order.code.OrderItemStatus;
import infitry.rest.api.repository.domain.order.code.OrderStatus;
import infitry.rest.api.repository.domain.user.Authority;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.repository.domain.user.code.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderItemTest {
    @Test
    public void 주문상품_취소() {
        // given
        Item item = Item.createItem("상품1", 1000, 30);
        String id = "test1";
        String password = "test2";
        Authority authority = Authority.createAuthority(Role.ROLE_ADMIN, "어드민 기능 허용");
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        User user = User.createUser(List.of(authority), UserDto.builder().id(id).password(password).addressDto(addressDto).build());
        OrderItem orderItem = OrderItem.createOrderItem(item, 1500, 3);
        Order order = Order.createOrder(user, orderItem);
        // when
        orderItem.cancel();
        // then
        assertEquals(OrderItemStatus.CANCEL, orderItem.getOrderItemStatus(), "주문상품 취소 시 상태가 CANCEL 이어야 한다.");
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus(), "전체 주문상품 취소 시 주문의 상태가 CANCEL 이어야 한다.");
        assertEquals(orderItem.getItem().getStockQuantity(), 30, "상품의 재고는 원복되어야 한다.");
    }

    @Test
    public void 주문상품_총가격() {
        // given
        Item item = Item.createItem("상품1", 1000, 30);
        // when
        int orderPrice = 1500;
        int orderCount = 3;
        OrderItem orderItem = OrderItem.createOrderItem(item, orderPrice, orderCount);
        // then
        assertEquals(orderPrice * orderCount, orderItem.getTotalPrice(), "총 상품금액은 (구매 당시 상품가격 * 상품 개수) 이어야 한다.");
    }
}