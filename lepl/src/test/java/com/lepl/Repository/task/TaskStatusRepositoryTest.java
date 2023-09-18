package com.lepl.Repository.task;

import com.lepl.domain.task.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
public class TaskStatusRepositoryTest {
    @Autowired
    TaskStatusRepository taskStatusRepository;

    /**
     * save, findOne
     */

    @Test
    @Transactional
    public void save_find() throws Exception {
        // given
        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);

        // when
        taskStatusRepository.save(taskStatus);
        TaskStatus findTaskStatus = taskStatusRepository.findOne(taskStatus.getId());

        // then
        Assertions.assertEquals(taskStatus, findTaskStatus);
        System.out.println(taskStatus.getCompletedStatus());
        System.out.println(taskStatus.getTimerOnOff());
    }
}