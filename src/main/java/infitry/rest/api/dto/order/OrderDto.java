package infitry.rest.api.dto.order;

import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.repository.domain.order.code.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    Long orderId;       // 주문 아이디
    UserDto userDto;    // 주문자
    List<OrderItemDto> orderItems; // 주문상품들
    LocalDateTime orderDate;    // 주문일
    OrderStatus orderStatus;    // 주문상태
}
