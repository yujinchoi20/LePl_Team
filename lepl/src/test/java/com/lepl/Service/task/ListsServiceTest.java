package com.lepl.Service.task;

import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
<<<<<<< HEAD
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.Month;
=======
import com.lepl.domain.task.TaskStatus;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
import java.util.ArrayList;
import java.util.List;


<<<<<<< HEAD
@SpringBootTest
@Transactional
//@Rollback(false) // db 확인위함
=======
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional // 쓰기모드 -> 서비스코드에 트랜잭션 유무 반드시 확인
@Slf4j
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
public class ListsServiceTest {
    @Autowired
    ListsService listsService;
    @Autowired
    EntityManager em;
<<<<<<< HEAD

    /**
     * save, findOne, findByDate, findAll, remove, findByToday
     */

    @Test
    public void save_find() throws Exception {
        // given
        LocalDateTime testDate = LocalDateTime.of(2022, Month.APRIL, 23, 12, 30); // test 날짜
        LocalDateTime testDate2 = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0); // test 날짜 1월.1일.0시.0분
        LocalDateTime testDate3 = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0); // test 날짜 1월.1일.0시.0분
        LocalDateTime testDate4 = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0); // test 날짜 2023년.6월.1일.12시.0분

        Member member = Member.createMember("12345",null);
        List<Task> tasks = new ArrayList<>();
        Task t1 = new Task();
        Task t2 = new Task();
        tasks.add(t1);
        tasks.add(t2);
        Lists lists1 = Lists.createLists(member, testDate, tasks);
        Lists lists2 = Lists.createLists(member, null, tasks); // 오늘 날짜 등록
        Lists list3 = Lists.createLists(member, testDate4, tasks); // findByToday 확인용

        // when
        em.persist(member); // 먼저 persist 필요
        listsService.join(lists1);
        listsService.join(lists2);
        listsService.join(list3);
        Lists findLists = listsService.findOne(lists1.getId());
        List<Lists> findLists_date = listsService.findByDate(testDate2, testDate3);
        List<Lists> findListsToday = listsService.findByCurrent(member.getId(), testDate4);

        // then
        Assertions.assertEquals(lists1, findLists);
        for(Lists getLists : findLists_date){
            System.out.println(getLists.getListsDate()); // 2023-05-28T19:12:47.619809700, 2022-04-23T12:30
        }
        System.out.println(findListsToday.size());
        System.out.println(findListsToday.get(0).getMember().getUid());
    }

    @Test
    public void delete() throws Exception {
        // given
        Lists lists = new Lists();
        listsService.join(lists);

        // when
        Lists findLists1 = listsService.findOne(lists.getId());
        listsService.remove(findLists1);
        Lists findLists2 = listsService.findOne(lists.getId());

        // then
        Assertions.assertEquals(findLists1, findLists2); // exp:findLists1, act:null
=======
    static List<Task> tasks; // 전역
    static Long memberId;
    static Long listsId;

    @BeforeEach // @Test 마다 수행 전 호출
    public void beforeEach() {
        tasks = new ArrayList<>(); // init
        // 2023-11-1~3 일정 임시로 추가 -> 각 task 는 2개씩
        for (int i = 1; i <= 3; i++) {
            LocalDateTime date_s = LocalDateTime.of(2023, 11, i, 1, 0); // 1시
            LocalDateTime date_e = LocalDateTime.of(2023, 11, i, 3, 0); // 3시
            LocalDateTime date2_s = LocalDateTime.of(2023, 11, i, 5, 0); // 5시
            LocalDateTime date2_e = LocalDateTime.of(2023, 11, i, 8, 0); // 8시
            TaskStatus taskStatus = TaskStatus.createTaskStatus(false,false);
            Task task = Task.createTask("조회 테스트입니다.", date_s, date_e, taskStatus);
            Task task2 = Task.createTask("조회 테스트입니다.2", date2_s, date2_e, taskStatus);
            tasks.add(task);
            tasks.add(task2);
        }
    }

    /**
     * findAllWithMemberTask, findByDateWithMemberTask, join, findByCurrent, findOneWithMemberTask, remove
     */
    @Test
    @Order(1)
    @Rollback(value = false) // 롤백 취소
    public void 멤버의_일정저장과조회_한개와지정날짜() throws Exception {
        // given
        log.info("size : {}", tasks.size());
        Member member = Member.createMember("Lists 테스트", "닉네임테스트");
        em.persist(member);
        memberId = member.getId(); // 전역 기록

        // when
        List<Lists> listsList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            LocalDateTime listsDate = LocalDateTime.of(2023, 11, i, 1, 0);
            Lists lists = Lists.createLists(member, listsDate, tasks);
            listsService.join(lists);
            listsList.add(lists);
        }
        listsId = listsList.get(0).getId();
        for (Task t : tasks) {
//            em.persist(t); // persist
        }

        // then
        for (int i = 1; i <= 3; i++) {
            LocalDateTime listsDate = LocalDateTime.of(2023, 11, i, 1, 0);
            Lists findLists = listsService.findByCurrent(member.getId(), listsDate); // flush
            Long listsId = findLists.getId();
            Lists findLists2 = listsService.findOneWithMemberTask(member.getId(), listsId); // flush
            Assertions.assertEquals(findLists.getId(), listsList.get(i - 1).getId());
            Assertions.assertEquals(findLists2.getId(), listsList.get(i - 1).getId());
        }
    }

    @Test
    @Order(2)
    public void 멤버의_일정조회_전체와날짜별() throws Exception {
        // given
        log.info("size : {}", tasks.size());
        // 2023-11-1~3 일정 조회위해 start, end 를 1~4
        LocalDateTime start = LocalDateTime.of(2023, 11, 1, 1, 0);
        LocalDateTime end = LocalDateTime.of(2023, 11, 4, 1, 0);

        // when
        List<Lists> findLists = listsService.findAllWithMemberTask(memberId);
        List<Lists> findLists2 = listsService.findByDateWithMemberTask(1L, start, end);

        // then
        for (Lists l : findLists) {
            log.info("findLists : {}", l.getId());
        }
        for (Lists l : findLists2) {
            log.info("findLists2 : {}", l.getListsDate());
        }
    }

    @Test
    @Order(3)
    @Rollback(value = false) // db 적용 확인위해
    public void 멤버의_일정_삭제() throws Exception {
        // given
        Lists findLists = listsService.findOneWithMemberTask(memberId, listsId);

        // when
        listsService.remove(findLists);
        Lists lists3 = listsService.findOne(3L);
        log.info("delete 쿼리문 날라가는 시점 체크1");
        findLists = listsService.findOneWithMemberTask(memberId, listsId);
        log.info("delete 쿼리문 날라가는 시점 체크2");

        // then
        Assertions.assertEquals(findLists, null);
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
    }
}