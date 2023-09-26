package com.lepl.Repository.character;

import com.lepl.domain.character.Exp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
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
class ExpRepositoryTest {
    @Autowired ExpRepository expRepository;

    @Test
    @Transactional
    public void 경험치_증가() throws Exception {
        //Given
        Exp exp = new Exp();
        exp.setExpValue(0l);
        exp.setExpAll(0l);
        exp.setLevel(1l);
        exp.setReqExp(1l);
        expRepository.save(exp);

        //When
        for(int i = 0; i < 15; i++) {
            exp.updateExp(1l, 1l);
            System.out.println("LEVEL: " + exp.getLevel());
            System.out.println("EXP: " + exp.getExpAll());
            System.out.println("TASK: " + exp.getPointTodayTask());
            System.out.println("TIMER: " + exp.getPointTodayTimer());

            if(exp.getPointTodayTask() >= 12) {
                System.out.println("already full");
            }
            if(exp.getPointTodayTimer() >= 12) {
                System.out.println("already full");
            }

            System.out.println("----------------------");
        }

        //Then
        System.out.println("LEVEL: " + exp.getLevel());
        System.out.println("EXP: " + exp.getExpAll());
        System.out.println("TASK: " + exp.getPointTodayTask());
        System.out.println("TIMER: " + exp.getPointTodayTimer());
    }

    @Test
    @Transactional
    public void 다음날_경험치() throws Exception {
        //Given


        //When


        //Then

    }
}