package com.lepl.Service.character;

import com.lepl.Repository.character.CharacterRepository;
import com.lepl.domain.character.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    @Autowired
    CharacterRepository characterRepository;

    //캐릭터 생성
    public Long save(Character character) {
        characterRepository.save(character);
        return character.getId();
    }

    //캐릭터 조회
    public Character findOne(Long id) {
        return characterRepository.findOne(id);
    }

    //캐릭터 삭제
    public void remove(Long id) {
        characterRepository.remove(id);
    }

}
