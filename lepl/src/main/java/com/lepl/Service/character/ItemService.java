package com.lepl.Service.character;

import com.lepl.Repository.character.ItemRepository;
import com.lepl.domain.character.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기 모드
@RequiredArgsConstructor
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    /*
    * save, findOne, findAll, remove
    * */

    /*
    * 사실 아이템 테이블은 사용자가 추가하는 것이 아니라 관리자가 새로운 아이템을 추가 하거나 삭제, 유지하는 함
    * 때문에 save, remove 가 많이 사용될거 같진 않음
    * findOne, findAll 과 같은 조회 메서드는 자주 사용될 듯
    * 그리고 item 장착 메서드를 추가하면 좋을듯! 아니면 비즈니스 로직을 추가하는 것도 좋을듯
    * */

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public Item findByName(String name) {
        return itemRepository.findByName(name);
    }
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Transactional
    public int updatePurchase(int purchase_quantity, Long itemId) {
        return itemRepository.updatePurchase(purchase_quantity, itemId);
    }

    @Transactional
    public void remove(Item item) {
        itemRepository.remove(item);
    }
}
