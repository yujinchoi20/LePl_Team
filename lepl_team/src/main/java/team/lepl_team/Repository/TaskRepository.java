package team.lepl_team.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import team.lepl_team.Domain.List.Lists;
import team.lepl_team.Domain.Task.Task;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TaskRepository {

    @PersistenceContext
    EntityManager em;

    //save, findOne, findAll, remove, update(수정)

    //업무 저장
    public void save(Task task) { //일단은 오늘 날짜의 업무만 추가 가능하도록 구현
        LocalDate today = LocalDate.now();

        Lists date = em.createQuery("select l from Lists l where l.date = :today", Lists.class)
                .setParameter("today", today)
                .getSingleResult();

        if(today.equals(date.getDate())) {
            date.setCount(date.getCount()+1);
            task.setLists(date);
            em.persist(task);
        }
    }

    //업무 하나 조회
    public Task findOne(Long id) {
        return em.find(Task.class, id);
    }

    //업무 전체 조회
    public List<Task> findAll() {
        return em.createQuery("select t from Task t", Task.class)
                .getResultList();
    }

    //업무 하나 삭제
    public void removeOne(Long id) {
        em.createQuery("delete from Task t where t.id = :id", Task.class)
                .setParameter("id", id);
        //업무 삭제 시 lists 테이블의 count 개수 하나 감소
    }

    //업무 전체 삭제
    public void removeAll() {
        em.createQuery("delete from Task t", Task.class);
        //전체 업무 삭제 시 lists 테이블의 count 개수 0으로 update
    }
}
