package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CharacterRepository {

    private final EntityManager em;

    /*
        save, findOne, remove
    */

    //캐릭터 생성
    public void save(Character character) {
        if(character.getId() == null) {
            em.persist(character);
        }
    }

    //캐릭터 조회
    public Character findOne(Long id) {
        return em.find(Character.class, id);
    }

    //캐릭터 화폐 업데이트
    public void updateCoin(Long money) {
        em.createQuery("update Character c set c.money =: money")
                .setParameter("money", money)
                .executeUpdate();
    }

    //캐릭터 삭제
    public void remove (Long id) {
        em.remove(id);
    }
}
