package team.lepl_team.Repository.Task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.lepl_team.Domain.Task.TaskStatus;

@Repository
@RequiredArgsConstructor
public class TaskStatusRepository {

    @PersistenceContext EntityManager em;

    public void save(TaskStatus taskStatus) {
        em.persist(taskStatus);
    }

    public TaskStatus findOne(Long id) {
        return em.find(TaskStatus.class, id);
    }
}
