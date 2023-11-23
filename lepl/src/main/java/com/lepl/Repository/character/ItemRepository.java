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

    //아이템 이름으로 검색
    public Item findByName(String name) {
        return (Item) em.createQuery("select i from Item i" +
                " where i.name =: name")
                .setParameter("name", name)
                .getSingleResult();
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i")
                .getResultList();
    }

    public int updatePurchase(int purchase_quantity, Long itemId) {
        return em.createQuery("update Item i set i.purchase_quantity =: purchase_quantity" +
                        " where i.id =: itemId")
                .setParameter("purchase_quantity", purchase_quantity)
                .setParameter("itemId", itemId)
                .executeUpdate();
    }

    public void remove(Item item) {
        em.remove(item);
    }
}
