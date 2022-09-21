package infitry.rest.api.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Schema(name = "SaveItemRequest", description = "상품등록 요청")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaveItemRequest {

    @NotBlank(message = "상품명이 올바르지 않습니다.")
    @Schema(name = "name", description = "상품명", type = "String", example = " ")
    String name;

    @Min(message = "가격은 최소 10원 이상 입니다.", value = 10)
    @Schema(name = "price", description = "상품 가격", type = "int", example = "0")
    int price;

    @PositiveOrZero(message = "재고는 양수와 0만 입력 가능합니다.")
    @Schema(name = "stockQuantity", description = "상품 수량", type = "int", example = "0")
    int stockQuantity;
}
