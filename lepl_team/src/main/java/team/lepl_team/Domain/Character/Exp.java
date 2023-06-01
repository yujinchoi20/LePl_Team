package team.lepl_team.Domain.Character;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Exp {

    @Id @GeneratedValue
    @Column(name = "exp_id")
    private Long id;

    private Long exp_all;
}
