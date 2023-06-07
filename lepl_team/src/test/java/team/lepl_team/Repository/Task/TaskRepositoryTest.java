package team.lepl_team.Repository.Task;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.Task.Task;
import team.lepl_team.Repository.Task.TaskRepository;

import java.util.List;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Test
//    @Rollback(false)
    public void 업무추가() throws Exception {
        //given
        Task task = new Task();
        task.setContent("delete 쿼리 해결");
        //task.setStartTime("12:00");
        //task.setEndTime("18:00");

        //when
        taskRepository.save(task);
        List<Task> taskAll = taskRepository.findAll();

        //then
        System.out.println(taskAll);
    }

    @Test
    public void 업무조회() throws Exception {
        //given
        Task task = new Task();
        task.setContent("JPA 공부!!");
        //task.setStartTime("20:00");
        //task.setEndTime("24:00");

        taskRepository.save(task);

        //when
        Task findOne = taskRepository.findOne(task.getId());

        //then
        Assertions.assertThat(task).isEqualTo(findOne);
    }

    @Test
    public void 전체_업무조회() throws Exception {
        //given
        Task task = new Task();
        task.setContent("JPA 공부!!");
        //task.setStartTime("20:00");
        //task.setEndTime("24:00");

        taskRepository.save(task);

        //when
        List<Task> findAll = taskRepository.findAll();

        //then
        Assertions.assertThat(findAll.size()).isEqualTo(2);
    }

    @Test
    public void 업무삭제() throws Exception {
        //given
        Task task = new Task();
        task.setContent("이거 지워져야 됨.");
        //task.setStartTime("16:00");
        //task.setEndTime("18:00");

        taskRepository.save(task);

        //when
        taskRepository.removeOne(task.getId());
        List<Task> findAll = taskRepository.findAll();

        //then
        Assertions.assertThat(findAll.size()).isEqualTo(3);
    }

    @Test
    public void 전체_업무삭제() throws Exception {
        //given
        Task task = new Task();
        task.setContent("삭제할거임");
        //task.setStartTime("17:00");
        //task.setEndTime("18:00");

        taskRepository.save(task);

        //when
        taskRepository.removeAll();
        List<Task> taskAll = taskRepository.findAll();

        //then
        Assertions.assertThat(taskAll.size()).isEqualTo(0);
    }
}