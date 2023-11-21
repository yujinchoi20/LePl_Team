package com.lepl.Repository.character;

import com.lepl.domain.character.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void 아이템_등록() throws Exception {
        //Given
        Item item = new Item();
        item.setType("Character");
        item.setName("Outer");
        item.setPrice(1);
        item.setPurchase_quantity(99);
        item.setStart_time(LocalDateTime.now());

        //When
        itemRepository.save(item);
        Item findItem = itemRepository.findOne(item.getId());

        //Then
        log.debug("item type = {}", findItem.getType());
        log.debug("item name = {}", findItem.getName());
        log.debug("item price = {}", findItem.getPrice());
        log.debug("item purchase = {}", findItem.getPurchase_quantity());

    }
}