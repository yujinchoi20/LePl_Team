package com.lepl.Service.character;

import com.lepl.Repository.character.ExpRepository;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //읽기 모드
@RequiredArgsConstructor
@Slf4j
public class ExpService {

    @Autowired
    private ExpRepository expRepository;

    /*
        save, findOne, findOneWithMember, remove, update, updatePoint
     */

    //경험치 쌓기
    @Transactional //쓰기 모드
    public Long save(Exp exp) {
        expRepository.save(exp);
        return exp.getId();
    }

    //경험치 조회 -> 현재 경험치, 누적 경험치
    public Exp findOne(Long id) {
        return expRepository.findOne(id);
    }
    public Member findOneWithMember(Long memberId) {return expRepository.findOneWithMember(memberId);}

    @Transactional //쓰기 모드
    public void remove(Exp exp) {
        expRepository.remove(exp);
    }

    //경험치 사용
    @Transactional //쓰기 모드
    public Exp update(Exp exp, Long pointTask, Long pointTimer) { //setter로 업데이트X
        return exp.updateExp(pointTask, pointTimer);
    }

    //매일 경험치 리셋
    @Scheduled(cron = "30 00 00 * * *") //매일 00시 00분 30초 획득 경험치 초기화
    @Transactional
    public void updatePoint() {
        log.debug("updatePoint Test");
        expRepository.updatePoint();
    }
}
