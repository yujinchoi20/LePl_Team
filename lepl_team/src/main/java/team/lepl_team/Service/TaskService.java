package team.lepl_team.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.Task.Task;
import team.lepl_team.Repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired TaskRepository taskRepository;

    //업무 저장
    public Long enroll(Task task) {
        taskRepository.save(task);
        return task.getId();
    }

    //업무 조회
    public Task findOne(Long id) {
        return taskRepository.findOne(id);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    //업무 수정
    @Transactional
    public void update(Task task, String content, String startTime, String endTime) {
        task.setContent(content);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
    }

    //업무 삭제
    @Transactional
    public void remove(Long id) {
        taskRepository.removeOne(id);
    }
}
