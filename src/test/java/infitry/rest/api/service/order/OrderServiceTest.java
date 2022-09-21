package infitry.rest.api.service.order;

import infitry.rest.api.dto.order.NewOrderItemDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.ItemRepository;
import infitry.rest.api.repository.OrderRepository;
import infitry.rest.api.repository.domain.item.Item;
import infitry.rest.api.repository.domain.order.Order;
import infitry.rest.api.repository.domain.order.code.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @Transactional
    public void 상품_주문하기() {
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
        // then
        assertEquals(userId, order.getUser().getUserId(), "주문자가 동일해야 한다.");
        assertEquals(2, order.getOrderItems().size(), "주문 상품의 개수는 2개 이어야 한다.");
        assertEquals(OrderStatus.ORDER, order.getOrderStatus(), "주문 초기상태는 ORDER 이어야 한다.");
    }

    @Test
    @Transactional
    public void 주문_전체취소() {
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
        orderService.cancelOrder(orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        // then
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus(), "해당 주문이 취소상태 이어야 한다.");
    }

    @Test
    public void 내_주문_검증하기() {
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
        // then
        assertThrows(ServiceException.class, () -> orderService.verifyMyOrder(orderId, 2L), "내 주문이 아니므로 Exception 이 발생하여야 한다.");
    }
}