package infitry.rest.api.service.item;


import infitry.rest.api.dto.item.ItemDto;
import infitry.rest.api.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    private ItemService itemService;

    @Test
    public void 상품등록_처리() {
        // given
        String itemName = "상품1";
        int price = 2000;
        int stock = 10;
        // when
        ItemDto savedItem = itemService.saveItem(ItemDto.builder()
                    .name(itemName)
                    .price(price)
                    .stockQuantity(stock)
                .build());
        // then
        assertEquals(itemName, savedItem.getName(), "상품명이 정상등록 되어야 한다.");
        assertEquals(price, savedItem.getPrice(),"가격이 정상등록 되어야 한다.");
        assertEquals(stock, savedItem.getStockQuantity(),"재고가 정상등록 되어야 한다.");
    }

    @Test
    public void 상품수정_처리() {
        // given
        String itemName = "상품1";
        String updateName = "상품2";
        int price = 2000;
        int updatePrice = 1000;
        int stock = 10;
        ItemDto savedItem = itemService.saveItem(ItemDto.builder()
                .name(itemName)
                .price(price)
                .stockQuantity(stock)
                .build());
        // when
        ItemDto updatedItem = itemService.updateItem(ItemDto.builder()
                .itemId(savedItem.getItemId())
                .name(updateName)
                .price(updatePrice)
                .stockQuantity(stock)
            .build());
        // then
        assertEquals(updatePrice, updatedItem.getPrice(),"가격은 수정 되어야 한다.");
        assertEquals(updateName, updatedItem.getName(),"이름은 수정 되어야 한다.");
        assertEquals(stock, updatedItem.getStockQuantity(),"재고는 변화가 없어야 한다.");
    }

    @Test
    public void 상품삭제_처리() {
        // given
        String itemName = "상품1";
        int price = 2000;
        int stock = 10;
        ItemDto savedItem = itemService.saveItem(ItemDto.builder()
                .name(itemName)
                .price(price)
                .stockQuantity(stock)
                .build());
        // when
        itemService.deleteItem(savedItem.getItemId());
        // then
        assertThrows(ServiceException.class, () -> itemService.findItem(savedItem.getItemId()), "저장된 상품이 없어야 한다.");
    }
}