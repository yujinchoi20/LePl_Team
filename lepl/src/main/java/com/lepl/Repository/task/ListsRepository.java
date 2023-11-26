package com.lepl.Repository.task;

import com.lepl.domain.task.Lists;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자 주입 + 엔티티 매니저 주입
public class ListsRepository {
    private final EntityManager em;

    public Lists findOne(Long id) {
        return em.find(Lists.class, id);
    }
    // 날짜 범위로 리스트(하루단위 일정들) 조회 => 이 함수는 사용안할것 같고, 필요없으면 지우기
    public List<Lists> findByDate(LocalDateTime start, LocalDateTime end) {
        return em.createQuery(
                        "select l from Lists l where l.listsDate >= :start and " +
                                "l.listsDate <= :end", Lists.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
    // 모든날의 리스트(하루단위 일정들) 조회 => 이 함수는 사용안할것 같고, 필요없으면 지우기
    public List<Lists> findAll() {
        return em.createQuery("select l from Lists l", Lists.class)
                .getResultList();
    }

    /**
     * 이 findByCurrent 함수가 Lazy+컬렉션이고, fetch join 을 안해서 좀 더 효과적인 경우
     * => 물론 fetch join 써도 되지만, 여기선 lists 존재 여부만 확인하면 되므로 애초에 추가로 쿼리 날라갈게 없이 1개로 끝남.
     * => 이것이 굳이 전부 Lazy 로 만들었을때의 장점 중에 하나
     *
     * 나머지 아래 함수들은 fetch join 이 필요해서 전부 사용한 경우 => 이 또한 쿼리문 1개로 만들어서 효과적
     */
    // fetch join 사용 => 중복 제거하고 싶으면 distinct 활용(마침 이곳을 테스트 해보니 join 문 때문에 중복이 발생)
    // fetch join 은 lazy 로 인한 쿼리문 여러번 나가는 비효율적인 방법을 쿼리문 1번으로 종결시켜주는 중요한 방법
    public List<Lists> findOneWithTask(Long listsId) {
        return em.createQuery(
                        "select distinct l from Lists l" +
                                " join fetch l.tasks t" +
                                " join fetch t.taskStatus ts" +
                                " where l.id = :listsId", Lists.class)
                .setParameter("listsId", listsId)
                .getResultList();
    }
    public List<Lists> findAllWithTask() {
        return em.createQuery(
                        "select distinct l from Lists l" +
                                " join fetch l.tasks t" +
                                " join fetch t.taskStatus ts", Lists.class)
                .getResultList();
    }







    //////////////////////////////////// 실제로 사용하는 함수들은 여기 아래부터 나오는 함수들 ////////////////////////////////////////

    /**
     * TaskApiController 에서 사용하는 함수들
     */
    public void save(Lists lists) {
        if(lists.getId() == null) {
            em.persist(lists);
        }
    }
    // 현재 들어온 날짜의 lists(=하루일정 모음) 가 있는지 조회 + memberId 까지 같이 사용 => 이렇게 해야 구분이 가능
    // 단, LocalDateTime -> DateTime 형태로 변환 후 비교 하겠음.
    public Lists findByCurrent(Long memberId, LocalDateTime curDate) {
        List<Lists> listsList = em.createQuery(
                        "select l from Lists l" +
                                " where l.member.id = :memberId and" +
                                " FORMATDATETIME(l.listsDate, 'yyyy-MM-dd') = :curDate", Lists.class)
                .setParameter("memberId", memberId)
                .setParameter("curDate", curDate.toLocalDate().toString())
                .getResultList();
        if(listsList.isEmpty()) return null;
        Lists lists = listsList.get(0);
        return lists;
    }

    /**
     * ListsApiController 에서 사용하는 함수들
     */
    public List<Lists> findAllWithMemberTask(Long memberId) {
        return em.createQuery(
                        "select distinct l from Lists l" +
                                " join fetch l.tasks t" +
                                " join fetch t.taskStatus ts" +
                                " where l.member.id = :memberId", Lists.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
    public List<Lists> findByDateWithMemberTask(Long memberId, LocalDateTime start, LocalDateTime end) {
        return em.createQuery(
                        "select distinct l from Lists l" +
                                " join fetch l.tasks t" +
                                " join fetch t.taskStatus ts" +
                                " where l.member.id = :memberId and" +
                                " FORMATDATETIME(l.listsDate, 'yyyy-MM-dd') >= :start and" +
                                " FORMATDATETIME(l.listsDate, 'yyyy-MM-dd') <= :end", Lists.class)
                .setParameter("memberId", memberId)
                .setParameter("start", start.toLocalDate().toString())
                .setParameter("end", end.toLocalDate().toString())
                .getResultList();
    }

    public Lists findOneWithMemberTask(Long memberId, Long listsId) {
        List<Lists> listsList = em.createQuery(
                        "select l from Lists l" +
                                " where l.member.id = :memberId and" +
                                " l.id = :listsId", Lists.class)
                .setParameter("memberId", memberId)
                .setParameter("listsId", listsId)
                .getResultList();
        if(listsList.isEmpty()) return null;
        Lists lists = listsList.get(0);
        return lists;
    }
    // 하루 일정 삭제 => lists(=하루 일정모음) 하나를 삭제하는 것!!
    public void remove(Lists lists) {
        em.remove(lists);
    }
}