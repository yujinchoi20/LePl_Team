package com.lepl.Service.character;

import com.lepl.Repository.character.ExpRepository;
import com.lepl.domain.character.Exp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //읽기 모드
@RequiredArgsConstructor
public class ExpService {

    @Autowired
    private ExpRepository expRepository;

    /*
        save, findOne, update, remove
     */

    //경험치 쌓기
    @Transactional //쓰기 모드
    public void save(Exp exp) {
        expRepository.save(exp);
    }

    //경험치 조회 -> 현재 경험치, 누적 경험치
    public Exp findOne(Long id) {
        return expRepository.findOne(id);
    }

    //경험치 사용
    @Transactional //쓰기 모드
    public void update(Long exp) { //setter로 업데이트X

    }

    //경험치 삭제
    @Transactional //쓰기 모드
    public void remove(Exp exp) {
        expRepository.remove(exp);
    }
}
