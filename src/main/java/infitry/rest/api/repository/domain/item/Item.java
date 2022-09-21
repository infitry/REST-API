package infitry.rest.api.repository.domain.item;

import infitry.rest.api.dto.item.ItemDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Entity(name = "tb_item")
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long itemId;

    @Column(nullable = false)
    String name;

    @Column
    int price;

    @Column
    int stockQuantity;

    private Item(String name, Integer price, Integer stock) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stock;
    }
    //== 생성 메서드 ==//
    public static Item createItem(String name, Integer price, Integer stock) {
        return new Item(name, price, stock);
    }

    //== 비지니스 로직 ==//
    /** 상품수정 */
    public Item updateItem(ItemDto itemDto) {
        this.name = itemDto.getName();
        this.price = itemDto.getPrice();
        this.stockQuantity = itemDto.getStockQuantity();
        return this;
    }

    /** 재고증가 */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /** 재고감소 */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new ServiceException("상품의 수량이 부족합니다.");
        }
        this.stockQuantity = restStock;
    }
}
