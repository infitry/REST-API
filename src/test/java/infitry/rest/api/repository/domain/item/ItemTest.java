package infitry.rest.api.repository.domain.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemTest {

    @Test
    public void 재고증가() {
        // given
        String name = "테스트상품";
        int price = 3000;
        int stockQuantity = 10;
        // when
        Item item = Item.createItem(name, price, stockQuantity);
        item.addStock(2);
        // then
        assertEquals(12, item.getStockQuantity(),"재고는 10에서 2가 증가된 12이여야 한다.");
    }

    @Test
    public void 재고감소() {
        // given
        String name = "테스트상품";
        int price = 3000;
        int stockQuantity = 10;
        // when
        Item item = Item.createItem(name, price, stockQuantity);
        item.removeStock(2);
        // then
        assertEquals(8, item.getStockQuantity(),"재고는 10에서 2가 감소된 8이여야 한다.");
    }

}