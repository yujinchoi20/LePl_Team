package com.lepl.Repository.character;

import com.lepl.domain.character.Item;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /*
    * save, findOne, findAll, remove
    * */

    public void save(Item item) {
        if(item.getId() == null) {
            em.persist(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i")
                .getResultList();
    }

    public void remove(Item item) {
        em.remove(item);
    }
}
