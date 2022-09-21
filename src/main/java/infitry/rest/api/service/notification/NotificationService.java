package infitry.rest.api.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class NotificationService {

    // 주문 알림
    public void sendOrderNotification() {
        log.info("주문이 처리되었습니다.");
    }

    // 주문 취소 알림
    public void sendOrderCancellationNotification() {
        log.info("주문취소가 정상 처리되었습니다.");
    }

    // 부분 취소 알림
    public void sendOrderItemCancellationNotification() {
        log.info("상품취소가 정상 처리되었습니다.");
    }
}
