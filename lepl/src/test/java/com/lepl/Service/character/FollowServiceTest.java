package com.lepl.Service.character;

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
class FollowServiceTest {

    @Autowired FollowService followService;
    @Autowired CharacterService characterService;
    @Autowired ExpService expService;

    @Test
    @Rollback(value = false)
    public void 팔로잉() throws Exception {
        //Given


        //When


        //Then
    }
}