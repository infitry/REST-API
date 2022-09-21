package infitry.rest.api.service.order;

import infitry.rest.api.dto.order.NewOrderItemDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.ItemRepository;
import infitry.rest.api.repository.OrderRepository;
import infitry.rest.api.repository.domain.item.Item;
import infitry.rest.api.repository.domain.order.Order;
import infitry.rest.api.repository.domain.order.OrderItem;
import infitry.rest.api.repository.domain.order.code.OrderItemStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderItemServiceTest {

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @Transactional
    public void 주문상품_부분취소() {
        // given
        Long userId = 1L;

        Item item1 = itemRepository.save(Item.createItem("상품1", 1000, 30));
        Item item2 = itemRepository.save(Item.createItem("상품2", 2000, 50));

        List<NewOrderItemDto> newOrderItems = new ArrayList<>();
        newOrderItems.add(NewOrderItemDto.builder()
                .itemId(item1.getItemId())
                .count(20)
                .build());
        newOrderItems.add(NewOrderItemDto.builder()
                .itemId(item2.getItemId())
                .count(30)
                .build());
        // when
        Long orderId = orderService.order(userId, newOrderItems);
        Order order = orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        OrderItem orderItem = order.getOrderItems().get(0);
        orderItemService.cancelOrderItem(orderItem.getOrderItemId());
        // then
        assertEquals(OrderItemStatus.CANCEL, orderItem.getOrderItemStatus(), "주문상품의 상태는 CANCEL 이어야 한다.");
        assertEquals(30, orderItem.getItem().getStockQuantity(), "상품의 원래 재고가 복구되어야 한다.");
    }

    @Test
    @Transactional
    public void 내_주문상품인지_검증하기() {
        // given
        Long userId = 1L;
        Item item1 = itemRepository.save(Item.createItem("상품1", 1000, 30));
        List<NewOrderItemDto> newOrderItems = new ArrayList<>();
        newOrderItems.add(NewOrderItemDto.builder()
                .itemId(item1.getItemId())
                .count(20)
                .build());
        // when
        Long orderId = orderService.order(userId, newOrderItems);
        Order order = orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        OrderItem orderItem = order.getOrderItems().get(0);
        // then
        ServiceException orderIdException = assertThrows(ServiceException.class,
                () -> orderItemService.verifyOrderItem(999L, orderItem.getOrderItemId(), userId), "주문에 속한 상품이 아니므로 Exception 이 발생하여야 한다.");
        assertEquals("해당 주문의 상품이 아닙니다.", orderIdException.getMessage());

        ServiceException userIdException = assertThrows(ServiceException.class,
                () -> orderItemService.verifyOrderItem(orderId, orderItem.getOrderItemId(), 2L), "내 주문상품이 아니므로 Exception 이 발생하여야 한다.");
        assertEquals("해당 사용자가 주문한 상품이 아닙니다.", userIdException.getMessage());
    }
}