package com.lepl.Service.task;

import com.lepl.domain.task.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Transactional
public class TaskStatusServiceTest {
    @Autowired
    TaskStatusService taskStatusService;

    /**
     * save, findOne
     */

    @Test
    public void save_find() throws Exception {
        // given
        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);

        // when
        taskStatusService.join(taskStatus);
        TaskStatus findTaskStatus = taskStatusService.findOne(taskStatus.getId());

        // then
        Assertions.assertEquals(taskStatus, findTaskStatus);
    }
}