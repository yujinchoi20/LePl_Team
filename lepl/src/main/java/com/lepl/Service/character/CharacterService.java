package com.lepl.Service.character;

import com.lepl.Repository.character.CharacterRepository;
import com.lepl.Repository.member.MemberRepository;
import com.lepl.domain.character.Character;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 읽기모드
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final MemberRepository memberRepository;

    /**
     * join, findOne, findCharacterWithMember, remove
     */
    @Transactional // 쓰기모드
    public Character join(Character character) {
        characterRepository.save(character);
        return character;
    }

    public Character findOne(Long characterId) {
        return characterRepository.findOne(characterId);
    }

    //캐릭터 화폐 업데이트
    @Transactional
    public void updateCoin(Long money, Long characterId) {
        characterRepository.updateCoin(money, characterId);
    }

    @Cacheable(value = "users", key = "#memberId") // [캐시 없으면 저장] 조회
    public Character findCharacterWithMember(Long memberId) {
        return characterRepository.findCharacterWithMember(memberId);
    }

    @Transactional // 쓰기모드
    public void remove(Character character) {
        characterRepository.remove(character);
    }
}