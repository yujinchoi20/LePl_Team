package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자 주입(엔티티매니저)
public class CharacterRepository {
    private final EntityManager em;

    /**
     * save, findOne, remove
     */
    public void save(Character character) {
        if(character.getId() == null) {
            em.persist(character);
        }
    }

    public Character findOne(Long id) {
        return em.find(Character.class, id);
    }

    //캐릭터 화폐 업데이트
    public void updateCoin(Long money, Long characterId) {
        em.createQuery("update Character c set c.money =: money" +
                        " where c.id =: characterId")
                .setParameter("money", money)
                .setParameter("characterId", characterId)
                .executeUpdate();
    }

    /**
     * memberId로 캐릭터 조회
     */
    public Character findCharacterWithMember(Long memberId) {
        List<Member> members = em.createQuery("select m from Member m " +
                        "join fetch m.character c " +
                        "where m.id = :memberId", Member.class)
                .setParameter("memberId", memberId)
                .getResultList();
        if(members.isEmpty()) return null;
        Character character = members.get(0).getCharacter();
        if(character==null) return null;
        else return character;
    }

    public void remove(Character character) { em.remove(character);}
}