package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//DB와 직접적으로 연결되는 코드
@Repository
public class ItemRepository {


    //멀티스레드 환경에서는 HaspMap x,ConcurrentHashMap o
    private static final Map<Long, Item> itemMap = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        itemMap.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return itemMap.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(itemMap.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        itemMap.clear();
    }
}
