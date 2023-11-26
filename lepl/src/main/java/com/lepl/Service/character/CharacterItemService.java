package com.lepl.Service.character;

import com.lepl.Repository.character.CharacterItemRepository;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.CharacterItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기모드
@RequiredArgsConstructor
public class CharacterItemService {
    private final CharacterItemRepository characterItemRepository;

    /**
     * save, findOne, findAll, remove
     */
    @Transactional // 쓰기모드
    public Long join(CharacterItem characterItem) { characterItemRepository.save(characterItem); return characterItem.getId(); }

    public CharacterItem findOne(Long characterItemId) { return characterItemRepository.findOne(characterItemId); }
    public List<CharacterItem> findAll() {return characterItemRepository.findAll();}

    //사용자 소유 아이템 전체 조회
    public List<CharacterItem> findAllWithMemberItem(Long characterId) {
        return characterItemRepository.findAllWithMemberItem(characterId);
    }

    //아이템 착용 여부
    @Transactional
    public void updateStatus(Long characterItemId, int status) {
        characterItemRepository.updateStatus(characterItemId, status);
    }

    @Transactional
    public void remove(CharacterItem characterItem) { characterItemRepository.remove(characterItem); }
}