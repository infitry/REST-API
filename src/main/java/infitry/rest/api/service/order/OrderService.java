package infitry.rest.api.service.order;

import infitry.rest.api.dto.order.NewOrderItemDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.ItemRepository;
import infitry.rest.api.repository.OrderItemRepository;
import infitry.rest.api.repository.OrderRepository;
import infitry.rest.api.repository.UserRepository;
import infitry.rest.api.repository.domain.item.Item;
import infitry.rest.api.repository.domain.order.Order;
import infitry.rest.api.repository.domain.order.OrderItem;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    /** 주문하기 */
    @Transactional
    public Long order(Long userId, List<NewOrderItemDto> newOrderItems) {
        log.debug("Ordered userId : {}", userId);
        log.debug("Ordered Items : {}", newOrderItems);
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException("주문 회원을 찾을 수 없습니다."));

        List<Item> items = makeItemsByNewOrderItems(newOrderItems);

        if (items.size() < 1) {
            throw new ServiceException("주문상품이 존재하지 않습니다.");
        }

        List<OrderItem> processedOrderItems = makeOrderItems(newOrderItems, items);

        Order newOrder = Order.createOrder(user, convertOrderItemArray(processedOrderItems));
        orderRepository.save(newOrder);
        orderItemRepository.saveAll(processedOrderItems);

        Long orderId = newOrder.getOrderId();
        sendOrderNotification(orderId);

        return orderId;
    }

    /** 주문취소하기 */
    @Transactional
    public void cancelOrder(Long orderId) {
        log.debug("orderId : {}", orderId);
        Order order = getOrder(orderId);
        order.cancel();
        sendOrderCancellationNotification(orderId);
    }

    /** 내 주문이 맞는지 확인 */
    public void verifyMyOrder(Long orderId, Long userId) {
        orderRepository.findByOrderIdAndUserUserId(orderId, userId).orElseThrow(() -> new ServiceException("해당하는 주문을 찾을 수 없습니다."));
    }

    /** List<OrderItem>을 Array 로 변환 */
    private static OrderItem[] convertOrderItemArray(List<OrderItem> processedOrderItems) {
        return processedOrderItems.toArray(OrderItem[]::new);
    }

    /** orderItem 생성 */
    private List<OrderItem> makeOrderItems(List<NewOrderItemDto> newOrderItems, List<Item> items) {
        return items.stream()
                .map(item -> {
                    int orderCount = newOrderItems.stream()
                            .filter(orderItem -> orderItem.getItemId().equals(item.getItemId()))
                            .mapToInt(NewOrderItemDto::getCount)
                            .findFirst().orElseThrow(() -> new ServiceException("정상적인 주문이 아닙니다."));
                    return OrderItem.createOrderItem(item, item.getPrice(), orderCount);
                }).collect(Collectors.toList());
    }

    /** itemId로 Item 조회 */
    private List<Item> makeItemsByNewOrderItems(List<NewOrderItemDto> orderItems) {
        return itemRepository.findAllById(orderItems.stream()
                .mapToLong(NewOrderItemDto::getItemId)
                .boxed()
                .collect(Collectors.toList())
        );
    }

    /** 주문 알림 발송 */
    private void sendOrderNotification(Long orderId) {
        try {
            notificationService.sendOrderNotification();
        } catch (Exception e) {
            // 알림 발송이 실패해도 주문은 정상처리
            log.info("주문 알림발송 실패 주문번호 : " + orderId, e);
        }
    }

    /** 주문취소 알림 발송 */
    private void sendOrderCancellationNotification(Long orderId) {
        try {
            notificationService.sendOrderCancellationNotification();
        } catch (Exception e) {
            // 알림 발송이 실패해도 주문취소는 정상처리
            log.info("주문취소 알림발송 실패 주문번호 : " + orderId, e);
        }
    }

    /** 주문 조회 */
    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ServiceException("해당 주문이 존재하지 않습니다."));
    }
}
