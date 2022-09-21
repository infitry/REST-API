package infitry.rest.api.controller.order;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.dto.order.NewOrderItemDto;
import infitry.rest.api.dto.order.OrderRequest;
import infitry.rest.api.service.order.OrderItemService;
import infitry.rest.api.service.order.OrderService;
import infitry.rest.api.util.ResponseUtil;
import infitry.rest.api.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Order API", description = "주문 API")
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "주문하기", description = "상품(들)을 주문한다.")
    public CommonResponse order(@Valid @RequestBody OrderRequest orderRequest) {
        List<NewOrderItemDto> orderItems = orderRequest.getOrderItemList().stream()
                    .map(orderItem -> modelMapper.map(orderItem, NewOrderItemDto.class))
                .collect(Collectors.toList());
        return ResponseUtil.getSingleResult(orderService.order(SecurityUtil.getLoginUserId(), orderItems));
    }

    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "주문취소하기", description = "주문을 취소한다.")
    public CommonResponse cancelOrder(@PathVariable Long orderId) {
        orderService.verifyMyOrder(orderId, SecurityUtil.getLoginUserId()); // 내 주문이 맞는지 검증
        orderService.cancelOrder(orderId);  // 주문 취소처리
        return ResponseUtil.successResponse();
    }

    @PatchMapping("/{orderId}/order-items/{orderItemId}/cancel")
    @Operation(summary = "주문상품 취소하기", description = "주문한 상품을 부분취소한다.")
    public CommonResponse cancelOrderItem(@PathVariable Long orderId, @PathVariable Long orderItemId) {
        orderItemService.verifyOrderItem(orderId, orderItemId, SecurityUtil.getLoginUserId());  // 유효한 상품인지 확인
        orderItemService.cancelOrderItem(orderItemId);  // 주문상품 취소처리
        return ResponseUtil.successResponse();
    }
}
