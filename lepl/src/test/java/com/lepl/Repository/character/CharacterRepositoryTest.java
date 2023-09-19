package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
class CharacterRepositoryTest {

    @Autowired
    CharacterRepository characterRepository;

    @Test
    public void 캐릭터_생성() {
        //Given
        Character character = new Character();

        //When


        //Then
    }

}