package com.lepl.Service.character;

import com.lepl.Repository.character.ExpRepository;
import com.lepl.domain.character.Exp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpService {

    @Autowired
    private ExpRepository expRepository;

    //경험치 쌓기
    public void save(Exp exp) {
        expRepository.save(exp);
    }

    //경험치 조회 -> 현재 경험치, 누적 경험치
    public Exp findOne(Long id) {
        return expRepository.findOne(id);
    }

    //경험치 사용
    public void remove() {

    }
}
