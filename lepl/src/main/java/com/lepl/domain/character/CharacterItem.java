package com.lepl.domain.character;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CHARACTER_ITEM")
public class CharacterItem {
    @Id
    @GeneratedValue
    @Column(name = "character_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    @NotNull
    private Long itemId; // 실제 아이템이 가지는 고유값(Null 불가)
    private Boolean wearingStatus; // 착용 유무 T/F

    /**
     * 생성 편의 메서드
     */
    public static CharacterItem createCharacterItem(Character character, Boolean wearingStatus, Long itemId) {
        CharacterItem characterItem = new CharacterItem();
        characterItem.character = character;
        characterItem.wearingStatus = wearingStatus;
        characterItem.itemId = itemId;

        return characterItem;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void setCharacter(Character character) {
        this.character = character;
    }
}