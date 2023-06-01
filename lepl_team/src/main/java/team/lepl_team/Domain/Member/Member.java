package team.lepl_team.Domain.Member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team.lepl_team.Domain.Character.Character;
import team.lepl_team.Domain.List.Lists;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String uid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    @OneToMany(mappedBy = "member") //양방향 매핑
    private List<Lists> lists = new ArrayList<>();
}
