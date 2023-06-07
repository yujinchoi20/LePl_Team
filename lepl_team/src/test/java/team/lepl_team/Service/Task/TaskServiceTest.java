package team.lepl_team.Service.Task;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.Task.Task;
import team.lepl_team.Service.Task.TaskService;

import java.util.List;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class TaskServiceTest {

    @Autowired
    TaskService taskService;

    @Test
    public void 업무추가() throws Exception {
        //given
        Task task = new Task();
        task.setContent("Task Test Case");

        //when
        taskService.enroll(task);
        Task findOne = taskService.findOne(task.getId());

        //then
        Assertions.assertThat(task).isEqualTo(findOne);
    }

    @Test
    public void 업무수정() throws Exception {
        //given
        Task task = new Task();
        task.setContent("Task Test Case");
        task.setStartTime("19:00");
        task.setEndTime("20:00");

        taskService.enroll(task);

        //when
        taskService.update(task, "업무 내용 수정", "20:00", "21:00");
        Task one = taskService.findOne(task.getId());

        //then
        System.out.println("CONTENT: " + one.getContent() + ", " + one.getStartTime() + "~" + one.getEndTime());
    }

    @Test
    public void 업무삭제() throws Exception {
        //given
        Task task = new Task();
        task.setContent("Task Test Case");
        task.setStartTime("19:00");
        task.setEndTime("20:00");

        taskService.enroll(task);

        //when
        taskService.remove(task.getId());
        List<Task> all = taskService.findAll();

        //then
        Assertions.assertThat(all.size()).isEqualTo(3);
    }
}