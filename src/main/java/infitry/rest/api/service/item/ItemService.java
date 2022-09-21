package infitry.rest.api.service.item;

import infitry.rest.api.dto.item.ItemDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.ItemRepository;
import infitry.rest.api.repository.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    /** 상품조회 */
    public ItemDto findItem(Long itemId) {
        Item findItem = itemRepository.findById(itemId).orElseThrow(() -> new ServiceException("상품을 찾을 수 없습니다."));
        return modelMapper.map(findItem, ItemDto.class);
    }

    /** 상품등록 */
    @Transactional
    public ItemDto saveItem(ItemDto itemDto) {
        Item savedItem = itemRepository.save(Item.createItem(itemDto.getName(), itemDto.getPrice(), itemDto.getStockQuantity()));
        return modelMapper.map(savedItem, ItemDto.class);
    }

    /** 상품수정 */
    @Transactional
    public ItemDto updateItem(ItemDto itemDto) {
        Item sourceItem = itemRepository.findById(itemDto.getItemId()).orElseThrow(() -> new ServiceException("해당 상품을 찾을 수 없습니다."));
        sourceItem.updateItem(itemDto);
        return modelMapper.map(sourceItem, ItemDto.class);
    }

    /** 상품삭제 */
    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
