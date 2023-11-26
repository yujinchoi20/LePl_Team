package com.lepl.domain.task;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {
    @Id
    @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long remainTime = 0L; // 잔여시간 -> 반환때는 시:분:초로!

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 1:1관계며 같이 존재함. (생명주기 같아야함)
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY) // cacade 필요한가?? 필요없다. Lists 는 Task 정보를 가지고 있지 않다. Task 가 오히려 주인이다.
    @JoinColumn(name = "lists_id")
    private Lists lists;


    //==생성 편의 메서드==//
    public static Task createTask(String content, LocalDateTime startTime, LocalDateTime endTime, TaskStatus taskStatus, Long remainTime) {
        Task task = new Task();
        task.content = content;
        task.startTime = startTime;
        task.endTime = endTime;
        task.taskStatus = taskStatus;
        task.remainTime = remainTime;
        return task;
    }

    //==연관관계 편의 메서드==//
    public void setLists(Lists lists) {
        this.lists = lists;
    }
    public void setId(Long id) {
        this.id = id;
    }

    //==비지니스 편의 메서드==//
    public Task updateTask(String content, LocalDateTime startTime, LocalDateTime endTime) {
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        return this;
    }

    public Task updateTaskStatus(TaskStatus taskStatus, Long remainTime) {
        this.taskStatus = taskStatus;
        this.remainTime = remainTime;
        return this;
    }
}