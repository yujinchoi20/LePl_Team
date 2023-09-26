package com.lepl.Service.character;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class NotificationServiceTest {

    @Autowired NotificationService notificationService;
    @Autowired FollowService followService;

    @Test
    @Rollback(value = false)
    public void 알림() throws Exception {
        //Given


        //When


        //Then
    }
}