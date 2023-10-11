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
    @Rollback(false)
    public void 캐릭터_생성() throws Exception{
        //Given
        Exp exp = new Exp();
        List<Follow> follows = new ArrayList<>();
        List<CharacterItem> characterItems = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();

        //Character character = Character.createCharacter(exp, characterItems, follows, notifications);
        Character character = characterRepository.findOne(1l); //
        character.setExp(exp); //여기서 setter를 사용하는게 맞는지 확신 없음.

        for(int i = 0; i < 2; i++) {
            CharacterItem characterItem = new CharacterItem();
            characterItem.setItemId(1l);
            characterItem.setWearingStatus(true);
            character.addCharacterItem(characterItem);
            characterItemRepository.save(characterItem);

            Follow follow = new Follow();
            character.addFollow(follow);
            followRepository.save(follow);
        }

        //When
        characterRepository.save(character);
        expRepository.save(exp);

        //Then
        System.out.println("Character_ID: " + character.getId());
        System.out.println("Character_EXP: " + character.getExp().getExpAll());
        System.out.println("Character Follows Num: " + character.getFollows().size());
        System.out.println("Character Items Num: " + character.getCharacterItems().size());
    }

}