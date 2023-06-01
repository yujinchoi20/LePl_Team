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

    @OneToMany(mappedBy = "character")
    private List<Character_Item> characterItems = new ArrayList<>();
}
