package com.lepl.domain.character;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Character {

    @Id @GeneratedValue
    @Column(name = "character_id") //PK
    private Long id;

    //coin 삭제함.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "exp_id") // FK
    private Exp exp;
    @OneToMany(mappedBy = "character") // 양방향
    private List<CharacterItem> characterItems = new ArrayList<>();

    @OneToMany(mappedBy = "character") // 양방향
    private List<Follow> follows = new ArrayList<>();

    /**
     * 연관관계 편의 메서드
     */
    public void addCharacterItem(CharacterItem characterItem) {
        characterItem.setCharacter(this); // CharacterItem(엔티티)에 Character(엔티티)참조
        this.characterItems.add(characterItem); // Character(엔티티)의 characterItems 리스트에 CharacterItem(엔티티)추가
    }
    public void addFollow(Follow follow) {
        follow.setCharacter(this);
        this.follows.add(follow);
    }

    /**
     * 생성 편의 메서드
     */
    public static Character createCharacter(Exp exp, List<CharacterItem> characterItems, List<Follow> follows) {
        Character character = new Character();
        character.setExp(exp);

        for(CharacterItem characterItem : characterItems) {
            character.addCharacterItem(characterItem); // 연관관계 편의 메서드
        }
        for(Follow follow : follows) {
            character.addFollow(follow); // 연관관계 편의 메서드
        }

        return character;
    }
}
