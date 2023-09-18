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
    public void create(Character character) {
        em.persist(character);
    }

    //캐릭터 찾기
    public Character findOne(Long id) {
        return em.find(Character.class, id);
    }

}
