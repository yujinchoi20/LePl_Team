package com.lepl.Repository.task;

import com.lepl.domain.task.Task;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자 주입 + 엔티티매니저 주입
public class TaskRepository {
    private final EntityManager em;

    /**
     * save, findOne, findAll, remove
     */
    public void save(Task task) {
        if(task.getId() == null){ // null 인 경우 db에 없다는 의미(db에 insert 할 때 id 생성)
            em.persist(task); // merge 사용 x => 더티 체킹
        }
    }

    public Task findOne(Long id) {
        return em.find(Task.class, id);
    }

    public List<Task> findAll() {
        return em.createQuery("select t from Task t", Task.class) // 올바르게 매핑해서 조회하기 위해서는 Task.class 가 필요
                .getResultList();
    }

    public List<Task> findOneWithMember(Long memberId, Long taskId) {
        return em.createQuery(
                "select distinct t from Task t" +
                        " join fetch t.lists l" +
                        " where t.id = :taskId and" +
                        " l.member.id = :memberId", Task.class)
                .setParameter("taskId", taskId)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public void remove(Task task) {
        em.remove(task);
    }
}