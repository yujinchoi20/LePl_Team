package com.lepl.Repository.task;

import com.lepl.domain.task.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


<<<<<<< HEAD

=======
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
@SpringBootTest
public class TaskStatusRepositoryTest {
    @Autowired
    TaskStatusRepository taskStatusRepository;

    /**
     * save, findOne
     */
<<<<<<< HEAD

    @Test
    @Transactional
    public void save_find() throws Exception {
=======
    @Test
    @Transactional // 롤백
    public void 일정상태_저장과조회() throws Exception {
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
        // given
        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);

        // when
<<<<<<< HEAD
        taskStatusRepository.save(taskStatus);
        TaskStatus findTaskStatus = taskStatusRepository.findOne(taskStatus.getId());

        // then
        Assertions.assertEquals(taskStatus, findTaskStatus);
        System.out.println(taskStatus.getCompletedStatus());
        System.out.println(taskStatus.getTimerOnOff());
=======
        taskStatusRepository.save(taskStatus); // persist
        // insert, select 쿼리 보려면 em.flush(), em.clear() 필수
        TaskStatus findTaskStatus = taskStatusRepository.findOne(taskStatus.getId());

        // then
        Assertions.assertEquals(taskStatus, findTaskStatus); // taskStatus 주소 그대로 사용 (캐시에 있으니)
        Assertions.assertEquals(taskStatus.getId(), findTaskStatus.getId());
        Assertions.assertEquals(findTaskStatus.getTimerOnOff(), false);
        Assertions.assertEquals(findTaskStatus.getCompletedStatus(), false);
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
    }
}