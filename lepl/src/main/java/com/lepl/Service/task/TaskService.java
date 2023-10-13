package com.lepl.Service.task;

import com.lepl.Repository.task.TaskRepository;
import com.lepl.domain.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기모드 기본 사용
@RequiredArgsConstructor // 생성자 주입 + 엔티티 매니저(서비스에서는 안씀)
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * 일정 추가
     */
    @Transactional // 쓰기모드 사용 위해
    public void join(Task task) {
        taskRepository.save(task);
    }

    /**
     * 일정 조회
     * 일정 전체 조회
     */
    public Task findOne(Long taskId) {
        return taskRepository.findOne(taskId);
    }
    public List<Task> findTasks() {
        return taskRepository.findAll();
    }
    public List<Task> findOneWithMember(Long memberId, Long taskId) {
        return taskRepository.findOneWithMember(memberId, taskId);
    }

    /**
     * 일정 삭제
     */
    @Transactional // 쓰기모드 사용 위해
    public void remove(Task task) {
        taskRepository.remove(task);
    }

    /*
    * 일정 업데이트
    * 1. 일정 추가
    * 2. 일정 잔여 시간 업데이트
    * */
    @Transactional // 쓰기모드 사용 위해 - db 적용
    public void update(Task task, String content, LocalDateTime startTime, LocalDateTime endTime) {
        task.setContent(content);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
    }

    @Transactional // 쓰기모드 사용 위해 - db 적용, 타이머 사용시 잔여시간 업데이트
    public void updateStatus(Task task, String content, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime remainTime) {
        task.setContent(content);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setRemainTime(remainTime);
    }
}