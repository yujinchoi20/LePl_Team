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

    /*@OneToMany(mappedBy = "member") //양방향 매핑 -> 연관관계 편의 메서드 필요!
    private List<Lists> lists = new ArrayList<>();

    //연관관계 편의 메서드 -> 양방향 연관관계가 걸린다.
    public void addLists(Lists list) {
        this.lists.add(list); //Member 엔티티의 lists에 받아온 list를 넣고
        list.setMember(this); //현재 나의 일정을 넣어준다.
    }*/
}
