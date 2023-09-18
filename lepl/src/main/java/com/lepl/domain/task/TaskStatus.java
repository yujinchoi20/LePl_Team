package com.lepl.domain.task;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter @Setter
@Table(name = "TASK_STATUS")
public class TaskStatus {
    @Id @GeneratedValue
    @Column(name = "task_status_id")
    private Long id;

    private Boolean completedStatus; // 일정 완료 유무
    private Boolean timerOnOff; // 타이머 사용 유무

    //==생성 편의 메서드==//
    public static TaskStatus createTaskStatus(Boolean completedStatus, Boolean timerOnOff) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setCompletedStatus(completedStatus);
        taskStatus.setTimerOnOff(timerOnOff);
        return taskStatus;
    }
}
