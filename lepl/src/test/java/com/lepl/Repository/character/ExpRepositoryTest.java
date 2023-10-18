package com.lepl.Repository.character;

import com.lepl.Repository.member.MemberRepository;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
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
//        Exp exp = new Exp();
//        exp.setExpValue(0l);
//        exp.setExpAll(0l);
//        exp.setLevel(1l);
//        exp.setReqExp(10l);
        //초기값: 현재 경험치=0, 누적 경험치=0, 레벨=1, 필요경험치=10

//        expRepository.save(exp);

        Exp exp = expRepository.findOne(652l);

        //When
        for(int i = 0; i < 8; i++) { //Timer 사용
            int min = 0; //타이머 획득 경험치 최소 0
            int max = 12; //타이머 획득 경험치 최대 12
            long timer = (long) ((Math.random() * (max - min)) + min); //타이머 사용시간을 난수로 설정

            exp.updateExp(1l, timer);
        }
        for(int i = 0; i < 12; i++) { //Timer 사용 안 함.
            exp.updateExp(1l, 0l);
        }

        //Then
        System.out.println("LEVEL: " + exp.getLevel());
        System.out.println("EXP: " + exp.getExpAll());
        System.out.println("TASK: " + exp.getPointTodayTask());
        System.out.println("TIMER: " + exp.getPointTodayTimer());
    }

    @Test
    @Transactional
    public void 회원찾기() throws Exception {
        //Given

        //When

        //Then
    }

}