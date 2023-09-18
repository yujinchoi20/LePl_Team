package com.lepl.Service.character;

import com.lepl.Repository.character.CharacterRepository;
import com.lepl.domain.character.Character;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //읽기 모드
@RequiredArgsConstructor //생성자 주입 방식 사용
public class CharacterService {

    private final CharacterRepository characterRepository;

    //캐릭터 생성
    @Transactional
    public Long create(Character character) {
        characterRepository.create(character);

        return character.getId();
    }

    //캐릭터 찾기
    public Character findOne(Long id) {
        return characterRepository.findOne(id);
    }
}
