package com.lepl.domain.task.timer;

import com.lepl.domain.task.Task;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Timer {
    @Id @GeneratedValue
    @Column(name = "timer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id") // FK
    private Task task;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TimerStatus timerStatus; // 집중상태 [허용앱 상태, 최대집중 상태]

    //==연관관계 편의 메서드==//
    /***
     * 명시적 setTask의 경우 Lombok의 setter보다 우선순위 => 애초에 개발이 끝나면 Lombok의 setter는 다 제거할 예정
     */
    public void setTask(Task task) {
        this.task = task;
        task.getTimers().add(this); // 기존 Lombok의 setter 로는 이부분 로직이 없음
    }

    //==생성 편의 메서드==//
    static public Timer createTimer(Task task, LocalDateTime start, LocalDateTime end, TimerStatus status) {
        Timer timer = new Timer();
        timer.setTask(task); // 롬복이 아닌 명시적으로 만든 setTask() 함수를 꼭 사용
        timer.setStartTime(start);
        timer.setEndTime(end);
        timer.setTimerStatus(status);
        return timer;
    }
}
