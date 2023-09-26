package com.lepl.Repository.character;

import com.lepl.domain.character.CharacterItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CharacterItemRepository {

    private final EntityManager em;

    /*
        save, findOne, findAll, remove
    */

    //아이템 저장
    public void save(CharacterItem characterItem) {
        if(characterItem.getId() == null) {
            em.persist(characterItem);
        }
    }

    //아이템 1개 조회
    public CharacterItem findOne(Long id) {
        return em.find(CharacterItem.class, id);
    }

    //전체 아이텀 조회
    public List<CharacterItem> findAll() {
        return em.createQuery("select i from CharacterItem i", CharacterItem.class)
                .getResultList();
    }

    //소유 아이템 삭제
    public void remove(CharacterItem characterItem) {
        em.remove(characterItem);
    }
}
