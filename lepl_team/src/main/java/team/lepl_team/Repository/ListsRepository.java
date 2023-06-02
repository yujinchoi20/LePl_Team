package team.lepl_team.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.lepl_team.Domain.List.Lists;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ListsRepository {

    @PersistenceContext
    EntityManager em;

    //일정 추가 -> Lists 테이블에는 id도 고유한 값이어야 함. 그리고 date도 고유한 값이어야 한다.
    public void save(Lists lists) {
        em.persist(lists);
    }

    //일정 하나 찾기
    public Lists findOne(Long listsId) {
        return em.find(Lists.class, listsId);
    }

    //특정 날짜의 일정 찾기
    public List<Lists> findByDate(LocalDate date){
        return em.createQuery("select l from Lists l where l.date = :date", Lists.class)
                .setParameter("date", date)
                .getResultList();
    }

    //회원 일정 모두 찾기 - 날짜 사용
    public List<Lists> findAll() {
         return em.createQuery("select l from Lists l", Lists.class)
                .getResultList();
    }
}
