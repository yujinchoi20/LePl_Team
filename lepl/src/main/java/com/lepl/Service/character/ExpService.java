package com.lepl.Service.character;

import com.lepl.Repository.character.ExpRepository;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true) // 읽기모드
@RequiredArgsConstructor
public class ExpService {
    private final ExpRepository expRepository;

    /**
     * join, findOne, findOneWithMember, remove, update, initPointToday
     */
    @Transactional // 쓰기모드
    public Exp join(Exp exp) {
        expRepository.save(exp);
        return exp;
    }

    public Exp findOne(Long expId) {
        return expRepository.findOne(expId);
    }

    public Exp findOneWithMember(Long memberId) {
        return expRepository.findOneWithMember(memberId);
    }

    @Transactional
    public void remove(Exp exp) {
        expRepository.remove(exp);
    }

    @Transactional
    public Exp update(Exp exp, Long pointTask, Long pointTimer) {
        return exp.updateExp(pointTask, pointTimer);
    }

    // 초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-6) (0: 일, 1: 월, 2:화, 3:수, 4:목, 5:금, 6:토)
    @Scheduled(cron = "30 00 00 * * *") // 00시 00분 30초 마다 수행
    @Transactional
    public void initPointToday() {
        log.debug("initPointToday Test");
        expRepository.initPointToday();
    }
}