package team.lepl_team.Repository.Task;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class TaskStatusRepositoryTest {

    @Autowired TaskRepository taskRepository;

    public void 상태저장() throws Exception {

    }

    public void 상태조회() throws Exception {

    }
}