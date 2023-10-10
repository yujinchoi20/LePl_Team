package com.lepl.Repository.task.timer;

import com.lepl.domain.task.Lists;
import com.lepl.domain.task.timer.Timer;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자 주입 + 엔티티 매니저 주입
public class TimerRepository {
    private final EntityManager em;

    /**
     * save, findOne, findAll
     */
    @Transactional
    public void save(Timer timer) {
        em.persist(timer);
    }
    public Timer findOne(Long id) {
        return em.find(Timer.class, id);
    }
    public List<Timer> findAll() {
        return em.createQuery("select l from Timer l", Timer.class)
                .getResultList();
    }
    public List<Timer> findAllWithTask(Long taskId) {
        return em.createQuery(
                        "select t from Timer t" +
                                " where t.task.id = :taskId", Timer.class)
                .setParameter("taskId", taskId)
                .getResultList();
    }
}
