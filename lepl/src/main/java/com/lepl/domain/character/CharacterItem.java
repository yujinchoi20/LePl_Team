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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id") //FK
    private Character character;

    @NotNull
    private Long itemId; // 실제 아이템이 가지는 고유값(Null 불가)
    private Boolean wearingStatus; // 착용 유무 T/F

    //private int itemCount; //보유하고 있는 아이템 총 개수
    //아이템의 총 개수도 표시하면 어떨까요??
}
