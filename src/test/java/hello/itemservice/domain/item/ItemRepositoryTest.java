package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {
    ItemRepository itemRepository = new ItemRepository();

    // 테스트 끝날때마다 실행
    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }

    @Test
    void save(){
        //given
        Item item = new Item("itemA",10000,10);
        //when
        itemRepository.save(item);
        //then
        Item findItem = itemRepository.findbyId(item.getId());
        assertThat(findItem).isEqualTo(item);
    }

    @Test
    void updateItem(){
        //given
        Item itemA = new Item("itemA",10,10000);
        Item savedItem = itemRepository.save(itemA);
        Long itemId = savedItem.getId();
        //when
        Item updateParam = new Item("item2",20,20);
        itemRepository.update(itemA.getId(),updateParam);
        //then
        Item findItem = itemRepository.findbyId(itemId);
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());

    }

    @Test
    void findAll(){
        //given
        Item itemA = new Item("itemA",10,10000);
        Item itemB = new Item("itemB",20,20000);
        itemRepository.save(itemA);
        itemRepository.save(itemB);
        //when
        List<Item> items = itemRepository.findAll();
        //then
        assertThat(items.size()).isEqualTo(2);
        assertThat(items).contains(itemA,itemB);
    }
}