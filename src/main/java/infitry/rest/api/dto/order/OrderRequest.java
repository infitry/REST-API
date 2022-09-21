package infitry.rest.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(name = "OrderRequest", description = "주문 요청")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @Schema(name = "orderItemList", description = "상품아이디와 주문 개수")
    @NotNull(message = "주문 상품을 선택해 주세요.")
    List<OrderItemRequest> orderItemList;
}
