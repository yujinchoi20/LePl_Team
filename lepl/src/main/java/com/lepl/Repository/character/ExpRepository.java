package com.lepl.Repository.character;

import com.lepl.domain.character.Exp;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExpRepository {

    private final EntityManager em;

    //경험치 쌓기
    public void save(Exp exp) {
        em.persist(exp);
    }

    //경험치 조회
    public Exp findOne(Long id) {
        return em.find(Exp.class, id);
    }

    //경험치 사용 -> 아이템 구매로 경험치 감소, 누적 경험치는 저장
    public void remove() {
        //??
    }
}
