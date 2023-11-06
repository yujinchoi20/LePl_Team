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

    @OneToOne(fetch = FetchType.LAZY) //지연 로딩, 단방향
    @JoinColumn(name = "item_id") //FK
    private Item item;

    /**
     * 여기에도 Character와의 연관관계 편의 메서드를 해줘야 할거 같지만 생성하면 안됨.
     * 만약 여기에도 Character와의 연관관계를 설정하면 무한루프에 빠질 수 있음.
     */

}
