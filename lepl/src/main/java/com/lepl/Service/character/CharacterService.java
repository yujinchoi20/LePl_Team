package com.lepl.Service.character;

import com.lepl.Repository.character.CharacterRepository;
import com.lepl.domain.character.Character;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //읽기 모드
@RequiredArgsConstructor
public class CharacterService {

    @Autowired
    CharacterRepository characterRepository;

    /*
        save, findOne, remove
    */

    //캐릭터 생성
    @Transactional //쓰기 모드
    public Long save(Character character) {
        characterRepository.save(character);
        return character.getId();
    }

    //캐릭터 조회
    public Character findOne(Long id) {
        return characterRepository.findOne(id);
    }

    //캐릭터 삭제
    @Transactional //쓰기 모드
    public void remove(Long id) {
        characterRepository.remove(id);
    }

}
