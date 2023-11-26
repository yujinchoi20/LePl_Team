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

    /**
     * save, findOne, findAll, remove
     */
    public void save(CharacterItem characterItem) {
        if(characterItem.getId() == null) {
            em.persist(characterItem);
        }
    }

    public CharacterItem findOne(Long id) {
        return em.find(CharacterItem.class, id);
    }

    public List<CharacterItem> findAll() {
        return em.createQuery("select f from CharacterItem f", CharacterItem.class)
                .getResultList();
    }

    //사용자 소유 아이템 전체 조회
    public List<CharacterItem> findAllWithMemberItem(Long characterId) {
        return em.createQuery("select c from CharacterItem c" +
                        " where c.character.id =: characterId")
                .setParameter("characterId", characterId)
                .getResultList();
    }

    public void updateStatus(Long characterItemId, int status) {
        if(status == 1) {
            em.createQuery("update CharacterItem c set c.wearingStatus = :wearingStatus" +
                            " where c.id = :characterItemId")
                    .setParameter("wearingStatus", true)
                    .setParameter("characterItemId", characterItemId)
                    .executeUpdate();
        } else {
            em.createQuery("update CharacterItem c set c.wearingStatus = :wearingStatus" +
                            " where c.id = :characterItemId")
                    .setParameter("wearingStatus", false)
                    .setParameter("characterItemId", characterItemId)
                    .executeUpdate();
        }
    }

    public void remove(CharacterItem characterItem) { em.remove(characterItem);}
}