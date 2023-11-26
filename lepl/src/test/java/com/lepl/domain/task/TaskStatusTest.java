package com.lepl.domain.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskStatusTest {

    @Test
    public void 생성_편의메서드() throws Exception {
        // given
        TaskStatus taskStatus;

        // when
        taskStatus = TaskStatus.createTaskStatus(true, false);

        // then
        Assertions.assertInstanceOf(TaskStatus.class, taskStatus);
    }
}