package infitry.rest.api.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Schema(name = "UpdateItemRequest", description = "상품수정 요청")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateItemRequest {
    @Schema(name = "name", description = "상품명", type = "String", example = " ")
    @NotBlank(message = "상품명은 필수 입니다.")
    String name;

    @Min(message = "가격은 최소 10원 이상 입니다.", value = 10)
    @Schema(name = "price", description = "상품 가격", type = "int", example = "10")
    int price;

    @PositiveOrZero(message = "재고는 양수와 0만 입력 가능합니다.")
    @Schema(name = "stockQuantity", description = "상품 수량", type = "int", example = "0")
    int stockQuantity;
}
