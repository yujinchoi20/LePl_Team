package com.lepl.domain.character;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Character {
    @Id
    @GeneratedValue
    @Column(name = "character_id")
    private Long id;

    private Long money = 0l; //Long 타입: 경험치를 화폐로 변경해서 사용하기 때문에 경험치와 같은 타입으로 선언
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exp_id") // FK
    private Exp exp;
    @OneToMany(mappedBy = "character") // 양방향
    private List<CharacterItem> characterItems = new ArrayList<>();
    @OneToMany(mappedBy = "character") // 양방향
    private List<Follow> follows = new ArrayList<>();
    @OneToMany(mappedBy = "character") // 양방향
    private List<Notification> notifications = new ArrayList<>();

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

    public void addNotification(Notification notification) {
        notification.setCharacter(this);
        this.notifications.add(notification);
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 생성 편의 메서드
     */
    public static Character createCharacter(Exp exp, List<CharacterItem> characterItems, List<Follow> follows, List<Notification> notifications) {
        Character character = new Character();
        character.exp = exp;
        for (CharacterItem characterItem : characterItems) {
            character.addCharacterItem(characterItem); // 연관관계 편의 메서드
        }
        for (Follow follow : follows) {
            character.addFollow(follow); // 연관관계 편의 메서드
        }
        for (Notification notification : notifications) {
            character.addNotification(notification); // 연관관계 편의 메서드
        }
        return character;
    }
}