package infitry.rest.api.dto.order;

import infitry.rest.api.dto.item.ItemDto;
import infitry.rest.api.repository.domain.order.code.OrderItemStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemDto {
    Long orderItemId;
    OrderDto order;
    ItemDto item;
    int orderPrice;
    int count;
    LocalDateTime cancelDate;
    OrderItemStatus orderItemStatus;
}
