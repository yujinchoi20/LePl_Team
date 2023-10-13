package com.lepl.domain.task;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Task {
    @Id @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime remainTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 1:1관계며 같이 존재함. (생명주기 같아야함)
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY) // cascade 필요한가?? 필요없다. Lists 는 Task 정보를 가지고 있지 않다. Task 가 오히려 주인이다.
    @JoinColumn(name = "lists_id")
    private Lists lists;

    //==생성 편의 메서드==//
    public static Task createTask(String content, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime remainTime, TaskStatus taskStatus) {
        Task task = new Task();
        task.content = content;
        task.startTime = startTime;
        task.endTime = endTime;
        task.remainTime = remainTime;
        task.taskStatus = taskStatus;
        return task;
    }
}
