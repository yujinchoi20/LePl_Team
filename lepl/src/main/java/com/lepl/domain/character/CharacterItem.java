package com.lepl.domain.character;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter @Setter
@Table(name = "CHARACTER_ITEM")
public class CharacterItem {
    @Id @GeneratedValue
    @Column(name = "character_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩, 양방향
    @JoinColumn(name = "character_id") //FK
    private Character character;

    private Boolean wearingStatus; // 착용 유무 T/F
    private Long itemId; //아이템 정보

    /*
        캐릭터 아이템 생성 편의 메서드
     */
    public static CharacterItem createCharacterItem(Character character, Boolean wearingStatus, Long itemId) {
        CharacterItem characterItem = new CharacterItem();
        characterItem.setCharacter(character);
        characterItem.setWearingStatus(wearingStatus);
        characterItem.setItemId(itemId);

        return characterItem;
    }
}
