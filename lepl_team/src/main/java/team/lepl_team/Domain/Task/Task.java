package team.lepl_team.Domain.Task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team.lepl_team.Domain.List.Lists;
import team.lepl_team.Domain.Timer.Timer;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Task {

    @Id @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private String content;
    private String startTime;
    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lists_id")
    private Lists lists;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_status_id")
    private Task_Status task_status;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL) //양방향 매핑
    private List<Timer> timers = new ArrayList<>();

    //연관관계 편의 메서드
    public void addTimer(Timer timer) {
        this.timers.add(timer);
        timer.setTask(this);
    }
}
