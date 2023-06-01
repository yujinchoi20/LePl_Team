package team.lepl_team.Domain.Character;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Coin {

    @Id @GeneratedValue
    @Column(name = "coin_id")
    private Long id;

    private Long coin_all;
}
