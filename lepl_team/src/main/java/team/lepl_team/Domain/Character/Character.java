package team.lepl_team.Domain.Character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Character {

    @Id @GeneratedValue
    @Column(name = "character_id")
    private Long id;

    private int level;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exp_id")
    private Exp exp;

    @OneToMany(mappedBy = "character") //양방향 매핑
    private List<Character_Item> characterItems = new ArrayList<>();

    //연관관계 편의 메서드
    public void addCharacterItem(Character_Item character_item) {
        this.characterItems.add(character_item); //현재 Character 엔티티의 characterItems 리스트에 받아온 Character_Item 엔티티 추가
        character_item.setCharacter(this); //Character 엔티티에 Character_Item 추가
    }
}
