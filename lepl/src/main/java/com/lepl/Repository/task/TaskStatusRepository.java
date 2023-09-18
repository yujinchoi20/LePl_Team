package com.lepl.Repository.task;

import com.lepl.domain.task.TaskStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor // 생성자 주입 + 엔티티 매니저
public class TaskStatusRepository {
    private final EntityManager em;

    /**
     * save, findOne
     */
    public void save(TaskStatus taskStatus) {
        em.persist(taskStatus);
    }
    public TaskStatus findOne(Long id) {
        return em.find(TaskStatus.class, id);
    }
}
