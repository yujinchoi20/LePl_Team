package team.lepl_team.Repository.Task;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.Task.TaskStatus;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class TaskStatusRepositoryTest {

    @Autowired TaskStatusRepository taskStatusRepository;

    @Test
    public void 상태저장_조회() throws Exception {
        //given
        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);

        //when
        taskStatusRepository.save(taskStatus);
        TaskStatus findTaskStatus = taskStatusRepository.findOne(taskStatus.getId());

        //then
        Assertions.assertThat(taskStatus).isEqualTo(findTaskStatus);
        System.out.println(findTaskStatus.getCompleteStatus());
        System.out.println(findTaskStatus.getTimerOnOff());
    }

}