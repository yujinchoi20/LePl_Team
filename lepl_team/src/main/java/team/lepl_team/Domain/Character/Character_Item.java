package team.lepl_team.Domain.Character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Character_Item {

    @Id @GeneratedValue
    @Column(name = "character_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    private Long itemId;

    @Enumerated(EnumType.STRING)
    private WearingStatus wearingStatus; //TRUE, FALSE
}
