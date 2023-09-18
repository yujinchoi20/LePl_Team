package com.lepl.Service.character;

import com.lepl.Repository.character.CharacterRepository;
import com.lepl.domain.character.Character;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CharacterServiceTest {

    @Autowired
    CharacterService characterService;

    @Autowired
    CharacterRepository characterRepository;

    @Test
    public void 캐릭터_생성() throws Exception {
        //Given
        Character character = new Character();

        //When
        Long createId = characterService.create(character);

        //Then
        Assertions.assertEquals(character, characterRepository.findOne(createId));
    }
}