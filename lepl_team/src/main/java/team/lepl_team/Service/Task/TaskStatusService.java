package team.lepl_team.Service.Task;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.lepl_team.Domain.Task.TaskStatus;
import team.lepl_team.Repository.Task.TaskStatusRepository;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    @Autowired
    TaskStatusRepository taskStatusRepository;

    public void save(TaskStatus taskStatus) {
        taskStatusRepository.save(taskStatus);
    }

    public TaskStatus findOne(Long id) {
        return taskStatusRepository.findOne(id);
    }
}
