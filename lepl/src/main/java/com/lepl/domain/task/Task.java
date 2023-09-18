package com.lepl.domain.task;

import com.lepl.domain.task.timer.Timer;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Task {
    @Id @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "task") // 양방향
    private List<Timer> timers = new ArrayList<>(); // cacade 필요한가?? 나중에 Timer 할때 고민해보겠다.

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 1:1관계며 같이 존재함. (생명주기 같아야함)
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY) // cacade 필요한가?? 필요없다. Lists 는 Task 정보를 가지고 있지 않다. Task 가 오히려 주인이다.
    @JoinColumn(name = "lists_id")
    private Lists lists;


    //==연관관계 편의 메서드==//
    public void addTimer(Timer timer) {
        timer.setTask(this); // Timer(엔티티)에 Task(엔티티)참조
        this.timers.add(timer); // Task(엔티티)의 timers 리스트에 Timer(엔티티)추가
    }
    //==생성 편의 메서드==//
    public static Task createTask(String content, LocalDateTime startTime, LocalDateTime endTime, TaskStatus taskStatus) {
        Task task = new Task();
        task.content = content;
        task.startTime = startTime;
        task.endTime = endTime;
        task.taskStatus = taskStatus;
        return task;
    }
}
