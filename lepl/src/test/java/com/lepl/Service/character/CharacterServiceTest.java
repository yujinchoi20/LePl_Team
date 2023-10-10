package com.lepl.Service.character;

import com.lepl.domain.character.*;
import com.lepl.domain.character.Character;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
class CharacterServiceTest {

    @Autowired
    CharacterService characterService;
    @Autowired
    CharacterItemService characterItemService;
    @Autowired
    ExpService expService;
    @Autowired
    FollowService followService;

    @Test
    public void 캐릭터_생성() throws Exception{
        //Given
        List<CharacterItem> characterItems = new ArrayList<>();
        Exp exp = new Exp();
        List<Follow> follows = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();

        exp.setExpAll(0l);
        exp.setExpValue(0l);
        exp.setLevel(1l);

        Character character = Character.createCharacter(exp, characterItems, follows, notifications);

        for(int i = 0; i < 2; i++) {
            CharacterItem characterItem = new CharacterItem();
            characterItem.setItemId(1l);
            characterItem.setWearingStatus(true);
            character.addCharacterItem(characterItem);
            characterService.save(character);

            Follow follow = new Follow();
            character.addFollow(follow);
            followService.save(follow);
        }

        //When
        characterService.save(character);
        expService.save(exp);

        //Then
        log.info("character.getId() : {}",character.getId());
        log.info("character.getExp().getExpAll() : {}",character.getExp().getExpAll());
        log.info("character.getExp().getExpValue() : {}",character.getExp().getExpValue());
        log.info("character.getExp().getLevel() : {}",character.getExp().getLevel());
        log.info("character.getCharacterItems().get(0).getItemId() : {}",character.getCharacterItems().get(0).getItemId());
    }
}