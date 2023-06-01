package team.lepl_team.Domain.Friends;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team.lepl_team.Domain.Member.Member;

@Entity
@Getter @Setter
public class Friends {

    @Id @GeneratedValue
    @Column(name = "friends_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //join 해서 닉네임 가져다 쓰기

}
