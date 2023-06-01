package team.lepl_team.Domain.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team.lepl_team.Domain.Member.Member;
import team.lepl_team.Domain.Task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Lists {

    @Id @GeneratedValue
    @Column(name = "lists_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //FK

    @OneToMany(mappedBy = "lists", cascade = CascadeType.ALL) //양방향 매핑
    private List<Task> tasks = new ArrayList<>();

    private LocalDateTime listDate;
}
