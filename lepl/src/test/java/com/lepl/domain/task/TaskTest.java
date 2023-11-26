package com.lepl.domain.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class TaskTest {

    @Test
    public void 생성_편의메서드() throws Exception {
        // given
        Task task;

        // when
        task = Task.createTask("테스트입니다.", LocalDateTime.now(), LocalDateTime.now(), new TaskStatus());

        // then
        Assertions.assertInstanceOf(Task.class, task);
    }

}