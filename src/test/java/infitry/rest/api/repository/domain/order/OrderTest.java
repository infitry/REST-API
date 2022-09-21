package infitry.rest.api.repository.domain.order;

import infitry.rest.api.dto.user.AddressDto;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.domain.item.Item;
import infitry.rest.api.repository.domain.order.code.OrderStatus;
import infitry.rest.api.repository.domain.user.Authority;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.repository.domain.user.code.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    public void 주문생성() {
        // given
        String id = "test1";
        String password = "test2";
        Authority authority = Authority.createAuthority(Role.ROLE_USER, "유저 기능 허용");
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        User user = User.createUser(List.of(authority), UserDto.builder().id(id).password(password).addressDto(addressDto).build());

        Item item1 = Item.createItem("상품1", 1000, 10);
        Item item2 = Item.createItem("상품2", 2000, 20);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, 1000, 2);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, 2000, 3);

        // when
        Order order = Order.createOrder(user, orderItem1, orderItem2);

        // then
        assertEquals(order.getOrderStatus(), OrderStatus.ORDER, "주문 생성 시 상태는 ORDER 이어야 한다.");
        assertEquals(order.getOrderItems().size(), 2, "주문한 상품 종류는 2개 이어야 한다.");
    }

    @Test
    public void 주문취소() {
        // given
        String id = "test1";
        String password = "test2";
        Authority authority = Authority.createAuthority(Role.ROLE_USER, "유저 기능 허용");
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        User user = User.createUser(List.of(authority), UserDto.builder().id(id).password(password).addressDto(addressDto).build());

        Item item1 = Item.createItem("상품1", 1000, 10);
        Item item2 = Item.createItem("상품2", 2000, 20);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, 1000, 2);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, 2000, 3);

        // when
        Order order = Order.createOrder(user, orderItem1, orderItem2);
        order.cancel();

        // then
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus(), "주문 취소 시 상태는 CANCEL 이어야 한다.");

        // when
        Order order2 = Order.createOrder(user, orderItem1, orderItem2);
        order2.setDeliveryCompletedStatus();

        // then
        assertThrows(ServiceException.class, order2::cancel, "배송이 완료되면 취소할 수 없습니다.");
    }

    @Test
    public void 전체_주문가격() {
        // given
        String id = "test1";
        String password = "test2";
        Authority authority = Authority.createAuthority(Role.ROLE_USER, "유저 기능 허용");
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        User user = User.createUser(List.of(authority), UserDto.builder().id(id).password(password).addressDto(addressDto).build());

        Item item1 = Item.createItem("상품1", 1000, 10);
        Item item2 = Item.createItem("상품2", 2000, 20);

        int orderItemPrice1 = 1000;
        int orderItemCount1 = 2;
        int orderItemPrice2 = 2000;
        int orderItemCount2 = 3;

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, orderItemPrice1, orderItemCount1);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, orderItemPrice2, orderItemCount2);

        // when
        Order order = Order.createOrder(user, orderItem1, orderItem2);
        int totalPrice = orderItemPrice1 * orderItemCount1 + orderItemPrice2 * orderItemCount2;

        // then
        assertEquals(totalPrice, order.getTotalPrice(), "총 가격은 모든 주문상품 (가격 * 수량)의 합이어야 한다.");

        // when
        orderItem1.cancel();

        // then
        int cancelTotalPrice = orderItemPrice2 * orderItemCount2;
        assertEquals(cancelTotalPrice, order.getTotalPrice(), "총 가격은 취소된 상품을 제외한 상품의 합이어야 한다.");
    }
}