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

    /**
     * save, findOne, findAll, findById, remove, findAllWithFollowing, findAllWithFollower
     */
    public void save(Follow follow) {
        if (follow.getId() == null) {
            em.persist(follow);
        }
    }

    public Follow findOne(Long id) {
        return em.find(Follow.class, id);
    }

    public List<Follow> findAll() {
        return em.createQuery("select f from Follow f", Follow.class)
                .getResultList();
    }

    /**
     * 중복 검증용 함수
     */
    public Follow findById(Long followerId, Long followingId) {
        List<Follow> follows = em.createQuery("select f from Follow f " +
                        "where f.followerId = :followerId " +
                        "and f.followingId = :followingId", Follow.class)
                .setParameter("followerId", followerId)
                .setParameter("followingId", followingId)
                .getResultList();
        if (follows.isEmpty()) return null;
        else return follows.get(0);
    }


    public void remove(Follow follow) {
        em.remove(follow);
    }

    public List<Follow> findAllWithFollowing(Long characterId) {
        return em.createQuery(
                        "select f from Follow f" +
                                " where f.character.id = :characterId", Follow.class)
                .setParameter("characterId", characterId)
                .getResultList();
    }

    public List<Follow> findAllWithFollower(Long characterId) {
        return em.createQuery(
                        "select f from Follow f" +
                                " where f.followingId = :characterId", Follow.class)
                .setParameter("characterId", characterId)
                .getResultList();
    }
}