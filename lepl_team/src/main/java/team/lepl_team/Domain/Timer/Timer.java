package team.lepl_team.Domain.Timer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team.lepl_team.Domain.Task.Task;

@Entity
@Getter @Setter
public class Timer {

    @Id @GeneratedValue
    @Column(name = "timer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task; //FK

    private String startTime;
    private String endTime;

    @Enumerated(EnumType.STRING)
    private TimerStatus timerStatus; //ALLOW, FOCUS

}
