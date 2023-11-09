package com.lepl.Repository.character;

import com.lepl.domain.character.*;
import com.lepl.domain.character.Character;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
class CharacterRepositoryTest {

    @Autowired CharacterRepository characterRepository;
    @Autowired ExpRepository expRepository;
    @Autowired FollowRepository followRepository;
    @Autowired CharacterItemRepository characterItemRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void 캐릭터_생성() throws Exception{
        //Given
        Exp exp = new Exp();
        List<Follow> follows = new ArrayList<>();
        List<CharacterItem> characterItems = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();

        Character character = new Character();
        character.setExp(exp); //여기서 setter를 사용하는게 맞는지 확신 없음.

        /*
        for(int i = 0; i < 2; i++) {
            CharacterItem characterItem = new CharacterItem();
            characterItem.setWearingStatus(true);
            character.addCharacterItem(characterItem);
            characterItemRepository.save(characterItem);

            Follow follow = new Follow();
            character.addFollow(follow);
            followRepository.save(follow);
        }
        */

        //When
        characterRepository.save(character);
        expRepository.save(exp);

        //Then
        log.debug("Character_ID: {}", character.getId());
        log.debug("Character_EXP: {}", character.getExp());
        log.debug("Character_Follow: {}", character.getFollows().size());
        log.debug("Character_Item: {}", character.getCharacterItems().size());
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void 화폐_업데이트() throws Exception { //-> 웬만하면 경험치부분에서 더티체킹으로 해결하기!!
        //Given
        Character character = characterRepository.findOne(1l);
        Exp exp = expRepository.findOne(1l);

        //When
        Long expAll = exp.getExpAll();
        characterRepository.updateCoin(expAll);

        //Then
        log.debug("expAll = {}", expAll);
        log.debug("character money = {}", character.getMoney());
    }
}