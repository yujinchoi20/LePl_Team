package com.lepl.Repository.character;

import com.lepl.domain.character.Follow;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

    private final EntityManager em;

    /*
        save, findOne, findAll, remove. findAllWithFollowing, findAllWithFollower
     */

    //친구 팔로우
    public void save(Follow follow) {
        if(follow.getId() == null) {
            em.persist(follow);
        }
    }

    //특정 친구 검색(조회)
    public Follow findOne(Long id) {
        return em.find(Follow.class, id);
    }

    //전체 친구 조회
    public List<Follow> findAll() {
        return em.createQuery("select f from Follow f", Follow.class)
                .getResultList();
    }

    //친구 팔로우 취소
    public void remove(Long id) {
        em.remove(id);
    }

    public List<Follow> findAllWithFollowing(Long characterId) {
        return em.createQuery("select f from Follow f" +
                " where f.character.id = :characterId", Follow.class)
                .setParameter("characterId", characterId)
                .getResultList();
    }

    public List<Follow> findAllWithFollower(Long characterId) {
        return em.createQuery("select f from Follow f" +
                " where f.followingId = :characterId", Follow.class)
                .setParameter("characterId", characterId)
                .getResultList();
    }
}
