package infitry.rest.api.dto.item;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long itemId;        // 상품 ID
    String name;        // 상품명
    int price;          // 상품가격
    int stockQuantity;  // 재고수량
}
