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

import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
class CharacterItemServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired CharacterRepository characterRepository;
    @Autowired ExpRepository expRepository;
    @Autowired CharacterItemRepository characterItemRepository;

    /*
        Character 화폐가 item 가격과 같거나 많을 경우 character_item에 item을 추가한다(구매)
        재구매 불가
        재고 확인
        한도 확인
     */
    @Test
    @Rollback(value = false)
    public void 아이템_구매_가능여부() throws Exception {
        //Given
        Item item = itemRepository.findOne(2l);
        Character character = characterRepository.findOne(1l);

        CharacterItem characterItem = new CharacterItem();
        characterItem.setWearingStatus(Boolean.FALSE);
        characterItem.setCharacter(character);

        int cnt = item.getPurchase_quantity(); //재고

        //When
        Long money = character.getMoney();
        int price = item.getPrice();

        Long wantItemId = item.getId();
        List<CharacterItem> getItems = characterItemRepository.findAll();

        for(CharacterItem items : getItems) {
            if(items.getItem().getId() == wantItemId) {
                throw new IllegalStateException("이미 소유한 아이템입니다.");
            }
        }

        if(money >= price && cnt >= 1) {
            characterItem.setItem(item); //아이템 구매
            cnt -= 1; //재고 감소

            characterItemRepository.save(characterItem); //캐릭터 소유 아이템 추가(캐릭터가 아이템을 구매)
            characterRepository.updateCoin(money - price); //아이템 구매에 사용한 만큼 화폐 차감
            item.setPurchase_quantity(cnt); //일단은 하나씩만 구매 가능
            log.debug("remain money = {}", (money - price));
        } else {
            log.debug("need more money = {}", (price - money));
            throw new IllegalStateException("아이템을 구매할 수 없습니다.(한도초과 혹은 재고부족)");
        }

        //Then
        log.debug("아이템 구매 가능 여부 확인 및 구매/반환");
    }

    @Test
    @Rollback(value = false)
    public void 아이템_착용여부_변경() throws Exception {
        //Given
        Item item = itemRepository.findOne(252l);
        Character character = characterRepository.findOne(1l);
        int status = 0;

        List<CharacterItem> characterItems = character.getCharacterItems();
        CharacterItem characterItem = null;

        for(CharacterItem c : characterItems) {
            if (c.getItem().getId() == item.getId()) {
                characterItem = c;
                break;
            }
        }

        //When
        if(characterItem == null) {
            throw new IllegalStateException("소유한 아이템이 아닙니다.");
        } else {
            //더티체킹으로 할 필요가 있음
            if(status == 1) {
                characterItemRepository.updateStatus(characterItem.getId(), status);
            } else {
                characterItemRepository.updateStatus(characterItem.getId(), status);
            }
        }

        //Then
        log.debug("착용 여부 {}", characterItem.getWearingStatus());
    }

    @Test
    @Transactional
    public void 착용여부_확인() throws Exception{
        //Given
        Item item = itemRepository.findOne(252l);
        Character character = characterRepository.findOne(1l);
        int status = 1;

        List<CharacterItem> characterItems = character.getCharacterItems();
        CharacterItem characterItem = null;

        //When
        for(CharacterItem c : characterItems) {
            if (c.getItem().getId() == item.getId()) {
                characterItem = c;
                break;
            }
        }

        //Then
        if(!characterItem.getWearingStatus() && status == 0 ||
            characterItem.getWearingStatus() && status == 1) {
            log.debug("상태 변경 안됨 {}", characterItem.getWearingStatus());
        }
    }
}