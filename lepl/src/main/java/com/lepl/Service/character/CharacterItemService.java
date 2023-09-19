package com.lepl.Service.character;

import com.lepl.Repository.character.CharacterItemRepository;
import com.lepl.domain.character.CharacterItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterItemService {

    @Autowired
    CharacterItemRepository characterItemRepository;

    //아이템 저장
    public void save(CharacterItem characterItem) {
        characterItemRepository.save(characterItem);
    }

    //아이템 1개 조회
    public CharacterItem findOnd(Long id) {
        return characterItemRepository.findOne(id);
    }

    //아이템 전체 조회
    public List<CharacterItem> findAll() {
        return characterItemRepository.findAll();
    }

    //소유 아이템 삭제
    public void remove(Long id) {
        characterItemRepository.remove(id);
    }
}
