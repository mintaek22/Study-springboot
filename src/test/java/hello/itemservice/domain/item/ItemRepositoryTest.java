package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("ItemA",1000,10);

        //when
        Item savedItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(savedItem);
    }


    @Test
    void findAll() {
        //given
        Item itemA = new Item("ItemA",1000,10);
        Item itemB = new Item("ItemB",2000,20);

        itemRepository.save(itemA);
        itemRepository.save(itemB);

        //when
        List<Item> items = itemRepository.findAll();

        //then
        assertThat(items.size()).isEqualTo(2);
        assertThat(items).contains(itemA, itemB);

    }

    @Test
    void updateItem() {
        //given
        Item itemA = new Item("ItemA",1000,20);

        itemRepository.save(itemA);

        //when
        Item itemB = new Item("ItemB",2000,20);

        itemRepository.update(itemA.getId(),itemB);

        //then
        Item findItem = itemRepository.findById(itemA.getId());
        assertThat(findItem.getItemName()).isEqualTo(itemB.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(itemB.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(itemB.getQuantity());
    }
}