package com.lepl.Repository.character;

import com.lepl.domain.character.Friend;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendRepository {

    private final EntityManager em;

    /*
        save, findOne, findAll, remove
     */

    //친구 팔로우
    public void save(Friend friend) {
        em.persist(friend);
    }

    //특정 친구 검색(조회)
    public Friend findOne(Long id) {
        return em.find(Friend.class, id);
    }

    //전체 친구 조회
    public List<Friend> findAll() {
        return em.createQuery("select f from Friend f", Friend.class)
                .getResultList();
    }

    //친구 팔로우 취소
    public void remove(Long id) {
        em.remove(id);
    }
}
