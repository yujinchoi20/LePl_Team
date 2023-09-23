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
        save, findOne, findAll, remove
     */

    //친구 팔로우
    public void save(Follow follow) {
        em.persist(follow);
    }

    //특정 친구 검색(조회)
    public Follow findOne(Long id) {
        return em.find(Follow.class, id);
    }

    //전체 친구 조회
    public List<Follow> findAll() {
        return em.createQuery("select f from Friend f", Follow.class)
                .getResultList();
    }

    //친구 팔로우 취소
    public void remove(Long id) {
        em.remove(id);
    }
}
