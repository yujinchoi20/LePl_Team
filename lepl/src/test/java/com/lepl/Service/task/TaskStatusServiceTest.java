package com.lepl.Service.task;

import com.lepl.domain.task.TaskStatus;
<<<<<<< HEAD
=======
import lombok.extern.slf4j.Slf4j;
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
<<<<<<< HEAD
@Transactional
=======
@Transactional // 쓰기모드 -> 서비스코드에 트랜잭션 유무 반드시 확인
@Slf4j
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
public class TaskStatusServiceTest {
    @Autowired
    TaskStatusService taskStatusService;

    /**
<<<<<<< HEAD
     * save, findOne
     */

    @Test
    public void save_find() throws Exception {
=======
     * join, findOne
     */

    @Test
    public void 일정상태_저장과조회() throws Exception {
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
        // given
        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);

        // when
        taskStatusService.join(taskStatus);
        TaskStatus findTaskStatus = taskStatusService.findOne(taskStatus.getId());

        // then
<<<<<<< HEAD
        Assertions.assertEquals(taskStatus, findTaskStatus);
=======
        Assertions.assertEquals(taskStatus.getId(), findTaskStatus.getId());
        Assertions.assertEquals(findTaskStatus.getTimerOnOff(), false);
        Assertions.assertEquals(findTaskStatus.getCompletedStatus(), false);
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
    }
}