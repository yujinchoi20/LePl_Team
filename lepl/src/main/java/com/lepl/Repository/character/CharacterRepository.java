package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CharacterRepository {

    private final EntityManager em;

    //캐릭터 생성
    public Long save(Character character) {
        em.persist(character);

        return character.getId();
    }

    //캐릭터 조회
    public Character findOne(Long id) {
        return em.find(Character.class, id);
    }

    //캐릭터 삭제
    public void remove (Long id) {
        em.remove(id);
    }
}
