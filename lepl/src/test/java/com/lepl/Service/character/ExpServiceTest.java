package com.lepl.Service.character;

import com.lepl.Repository.character.ExpRepository;
import com.lepl.domain.character.Exp;
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
class ExpServiceTest {

    @Autowired
    ExpRepository expRepository;
    @Autowired
    ExpService expService;

    /*
    * 00시 00분 30초에 경험치가 리셋되는지 확인!
    * */
    @Test
    @Transactional
//    @Rollback(value = false)
    public void 매일_경험치_리셋() throws Exception {
        //Given
        Exp exp = expService.findOne(652l);

        //When
        expService.updatePoint(); //경험치 초기화

        //Then
        log.debug("task exp : {}", exp.getPointTodayTask());
        log.debug("timer exp : {}", exp.getPointTodayTimer());
    }
}