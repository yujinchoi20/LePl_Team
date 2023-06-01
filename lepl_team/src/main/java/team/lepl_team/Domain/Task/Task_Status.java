package team.lepl_team.Domain.Task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Task_Status {

    @Id @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private Boolean completeStatus; //일정 완료 여부
    private Boolean timerOnOff; //타이머 사용 여부
}
