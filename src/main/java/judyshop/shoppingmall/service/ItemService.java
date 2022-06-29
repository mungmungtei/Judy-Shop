package judyshop.shoppingmall.service;

import judyshop.shoppingmall.domain.item.Item;
import judyshop.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {  // 변경 감지 기능 사용
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public Item findOne(Long iemId) {
        return itemRepository.findOne(iemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

}
