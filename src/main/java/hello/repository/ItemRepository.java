package hello.repository;

import hello.domain.item.Item;
import hello.domain.item.ItemSearchDto;

import java.util.List;

public interface ItemRepository {

    public Item save(Item item);

    public Item findById(Long id);

    public List<Item> findAll(ItemSearchDto itemSearchDto);

    public void update(Long id, Item updateParam);
}
