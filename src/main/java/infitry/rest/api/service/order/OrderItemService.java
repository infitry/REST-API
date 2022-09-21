package infitry.rest.api.service.order;

import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.OrderItemRepository;
import infitry.rest.api.repository.domain.order.OrderItem;
import infitry.rest.api.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final NotificationService notificationService;

    /** 주문상품 부분취소  */
    @Transactional
    public void cancelOrderItem(Long orderItemId) {
        OrderItem orderItem = getOrderItem(orderItemId);
        orderItem.cancel();
        sendOrderItemCancellationNotification(orderItemId);
    }

    /** 내가 주문한 상품이 맞는지 확인한다. */
    public void verifyOrderItem(Long orderId, Long orderItemId, Long userId) {
        OrderItem orderItem = findOrderItemByOrderItemId(orderItemId);

        if (!orderId.equals(orderItem.getOrder().getOrderId())) {
            throw new ServiceException("해당 주문의 상품이 아닙니다.");
        }

        if (!userId.equals(orderItem.getOrder().getUser().getUserId())) {
            throw new ServiceException("해당 사용자가 주문한 상품이 아닙니다.");
        }
    }

    private OrderItem findOrderItemByOrderItemId(Long orderItemId) {
        return orderItemRepository.findByOrderItemId(orderItemId).orElseThrow(() -> new ServiceException("주문한 상품을 찾을 수 없습니다."));
    }

    /** 주문상품 부분취소 알림 발송 */
    private void sendOrderItemCancellationNotification(Long orderItemId) {
        try {
            notificationService.sendOrderItemCancellationNotification();
        } catch (Exception e) {
            // 알림 발송이 실패해도 주문취소는 정상처리
            log.info("상품취소 알림발송 실패 주문번호 : " + orderItemId, e);
        }
    }

    /** 주문 상품을 가져온다 **/
    private OrderItem getOrderItem(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElseThrow(() -> new ServiceException("주문한 상품을 찾을 수 없습니다."));
    }
}
