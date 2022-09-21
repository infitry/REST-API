package infitry.rest.api.controller.item;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.dto.item.ItemDto;
import infitry.rest.api.dto.item.SaveItemRequest;
import infitry.rest.api.dto.item.UpdateItemRequest;
import infitry.rest.api.service.item.ItemService;
import infitry.rest.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Item API", description = "상품 API")
@RequestMapping(value = "/items")
@RequiredArgsConstructor
@RestController
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "상품 등록", description = "상품등록 처리")
    public CommonResponse saveItem(@Valid @RequestBody SaveItemRequest saveItemRequest) {
        return ResponseUtil.getSingleResult(itemService.saveItem(
            ItemDto.builder()
                    .name(saveItemRequest.getName())
                    .price(saveItemRequest.getPrice())
                    .stockQuantity(saveItemRequest.getStockQuantity())
                .build()
        ));
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "상품 수정", description = "상품수정 처리", parameters = {
            @Parameter(name = "itemId", example = " ")
    })
    public CommonResponse updateItem(@Valid @RequestBody UpdateItemRequest updateItemRequest, @PathVariable Long itemId) {
        return ResponseUtil.getSingleResult(itemService.updateItem(ItemDto.builder()
                    .itemId(itemId)
                    .name(updateItemRequest.getName())
                    .price(updateItemRequest.getPrice())
                    .stockQuantity(updateItemRequest.getStockQuantity())
                .build()
        ));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "상품 삭제", description = "상품삭제 처리")
    public CommonResponse deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseUtil.successResponse();
    }
}
