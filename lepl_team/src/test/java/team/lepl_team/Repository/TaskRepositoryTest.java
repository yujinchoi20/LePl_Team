package team.lepl_team.Repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.Task.Task;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class TaskRepositoryTest {

    @Autowired TaskRepository taskRepository;

    @Test
    @Rollback(value = false)
    public void 업무추가() throws Exception {
        //given
        Task task = new Task();
        task.setContent("JPA 공부!!");
        task.setStartTime("20:00");
        task.setEndTime("24:00");

        //when
        taskRepository.save(task);
        List<Task> taskAll = taskRepository.findAll();

        //then
        System.out.println(taskAll);
    }
}