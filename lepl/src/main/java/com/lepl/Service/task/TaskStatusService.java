package com.lepl.Service.task;

import com.lepl.Repository.task.TaskStatusRepository;
import com.lepl.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // 생성자 주입 + 엔티티 매니저(안사용)
@Transactional(readOnly = true) // 읽기 모드
public class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    /**
     * save, findOne
     */
    @Transactional // 쓰기 모드
    public void join(TaskStatus taskStatus) {
        taskStatusRepository.save(taskStatus);
    }
    public TaskStatus findOne(Long id) {
        return taskStatusRepository.findOne(id);
    }
}
