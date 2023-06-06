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
        } //else {Lists 에 해당 날짜로 컬럼을 추가 해야됨.}
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
    //@Query("DELETE FROM Task t WHERE t.id = :id")
    public void removeOne(Long id) { //오늘의 업무만 삭제 할 수 있도록 구현(수정할 것)
        //존재하는 업무만 삭제 가능
        Task findTask = em.createQuery("select t from Task t where t.id = :id", Task.class)
                .setParameter("id", id)
                .getSingleResult();

        if(findTask.getId() != null) { //업무가 존재한다면
            //해당 업무 삭제

            em.createQuery("delete from Task t where t.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            //오늘 날짜의 일정 컬럼을 찾고
            LocalDate today = LocalDate.now();

            Lists date = em.createQuery("select l from Lists l where l.date = :today", Lists.class)
                    .setParameter("today", today)
                    .getSingleResult();

            //일정 컬럼의 업무 개수를 1개 감소시킴
            if(today.equals(date.getDate())) {
                date.setCount(date.getCount() - 1);
                System.out.println("TODAY TASK : " + date.getCount());
            }
        }
    }

    //업무 전체 삭제
    public void removeAll() {
        //오늘 날짜의 전체 업무를 삭제하는 기능으로 구체화 시켜야함.
        em.createQuery("delete from Task t")
                .executeUpdate();
        //전체 업무 삭제 시 lists 테이블의 count 개수 0으로 update
    }
}
