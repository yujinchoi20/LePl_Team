package com.lepl.Service.character;

import com.lepl.Repository.character.*;
import com.lepl.domain.character.*;
import com.lepl.domain.character.Character;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class CharacterItemServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired CharacterRepository characterRepository;
    @Autowired ExpRepository expRepository;
    @Autowired CharacterItemRepository characterItemRepository;

    @Test
    @Rollback(value = false)
    public void 아이템_구매_가능여부() throws Exception {
        //Given
        Item item = itemRepository.findOne(52l);
        Character character = characterRepository.findOne(102l);

        CharacterItem characterItem = new CharacterItem();
        characterItem.setWearingStatus(Boolean.FALSE);
        characterItem.setCharacter(character);

        //When
        characterItemRepository.save(characterItem);
        CharacterItem findCharacterItem = characterItemRepository.findOne(characterItem.getId());
        Character findCharacter = findCharacterItem.getCharacter();
        Long expAll = findCharacter.getExp().getExpAll();

        //Then
        if(expAll >= item.getPrice()) {
            characterItem.setItem(item); //아이템 정보도 저장, Api 구현부에서 setter 사용 가능하나요..?? 일단 보류
            expRepository.updateBuyItem(expAll-item.getPrice()); //아이템 구매인한 경험치 업데이트
            log.debug("GET ITEM = {}", item.getPrice());
            log.debug("Character item = {}", characterItem.getItem().getName());
        } else {
            log.debug("MORE NEED EXP = {}", (item.getPrice() - expAll));
        }
    }
}