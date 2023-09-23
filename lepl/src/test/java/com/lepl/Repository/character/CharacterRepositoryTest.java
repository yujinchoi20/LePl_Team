package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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

    @Autowired ExpRepository expRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void 캐릭터_생성() throws Exception{
        //Given
        Character character = new Character();
        Exp exp = new Exp();
        exp.setExpAll(0l);
        exp.setExpValue(0l);
        exp.setLevel(1l);

        //When
        Long saveId = characterRepository.save(character);
        Long saveExpId = expRepository.save(exp);

        //Then
        log.info(saveId.toString());
        log.info(saveExpId.toString());
    }

}