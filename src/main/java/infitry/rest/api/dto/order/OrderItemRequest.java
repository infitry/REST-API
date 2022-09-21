package infitry.rest.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;

@Schema(name = "OrderItemRequest", description = "주문상품 요청")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {

    @Schema(name = "itemId", description = "상품 아이디", example = "0")
    @Min(value = 1, message = "주문상품의 아이디가 올바르지 않습니다.")
    Long itemId;

    @Schema(name = "count", description = "주문상품 개수", example = "0")
    @Min(value = 1, message = "주문상품의 개수가 올바르지 않습니다.")
    int count;
}
