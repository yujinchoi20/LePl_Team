package com.lepl.Service.character;

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
class CharacterServiceTest {

    @Autowired
    CharacterService characterService;
    @Autowired
    ExpService expService;
    @Autowired
    FriendService friendService;

    @Test
    public void 캐릭터_생성() {
        //캐릭터 생성시 레벨1 경험치0 친구0 세팅
        //Given


        //When


        //Then

    }
}