package com.lepl.Repository.character;

import com.lepl.Repository.member.MemberRepository;
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
class ExpRepositoryTest {
    @Autowired ExpRepository expRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CharacterRepository characterRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void 경험치_증가() throws Exception {
        //Given


        //When


        //Then
    }

}