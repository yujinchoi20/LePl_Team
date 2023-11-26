package com.lepl.Repository.task;

<<<<<<< HEAD
import com.lepl.Repository.member.MemberRepository;
import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import org.junit.jupiter.api.Test;
=======
import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.Month;
=======
import java.time.LocalDateTime;
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
import java.util.ArrayList;
import java.util.List;


<<<<<<< HEAD
@SpringBootTest
=======
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Slf4j
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
public class ListsRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    ListsRepository listsRepository;
    @Autowired
<<<<<<< HEAD
    MemberRepository memberRepository;
    // save, findOne, findByDate, findAll, remove, findByToday

    @Test
    @Transactional
    @Rollback(false)
    public void save_find_test() throws Exception {
        // given
        LocalDateTime testDate = LocalDateTime.of(2022, Month.APRIL, 23, 12, 30); // test 날짜
        LocalDateTime testDate2 = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0); // test 날짜 2022년.1월.1일.0시.0분
        LocalDateTime testDate3 = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0); // test 날짜 2024년.1월.1일.0시.0분
        LocalDateTime testDate4 = LocalDateTime.of(2023, Month.JUNE, 1, 12, 0); // test 날짜 2023년.6월.1일.12시.0분

        //Member member = new Member();
        //member.setUid("123"); // uid not null 상태라서 Member를 persist 할거라면, 테스트에서도 꼭 추가
        Member member = memberRepository.findByUid("123"); //DB에 미리 생성해둔 사용자를 사용함

        List<Task> tasks = new ArrayList<>();
        Task t1 = new Task();
        Task t2 = new Task();
        tasks.add(t1);
        tasks.add(t2);
        Lists list1 = Lists.createLists(member, testDate, tasks);
        Lists list2 = Lists.createLists(member, null, tasks); // null 이면 현재 날짜 자동 등록
        Lists list3 = Lists.createLists(member, testDate4, tasks); // findByToday 확인용

        // when
        em.persist(member);
        listsRepository.save(list1); // em.persist
        listsRepository.save(list2); // em.persist
        listsRepository.save(list3); // em.persist
        Lists findListsOne = listsRepository.findOne(list1.getId());
        List<Lists> findListsAll = listsRepository.findAll();
        List<Lists> findListsAll_date = listsRepository.findByDate(testDate2, testDate3);
        List<Lists> findListsToday = listsRepository.findByCurrent(member.getId(), testDate4);

        // then
        System.out.println(findListsOne.getListsDate()); // 2022-04-23T12:30
        System.out.println(findListsAll.get(findListsAll.size()-1).getListsDate()); // 2023-05-28T18:00:28.672150900
        System.out.println(findListsAll.size()); // 3

        System.out.println("findListsAll_date 개수 : "+findListsAll.size()); // findListsAll_date 개수 : 2
        for(int i = 0 ;i <findListsAll_date.size();i++){
            System.out.println(findListsAll_date.get(i).getListsDate()); // 2022-04-23T12:30, 2023-05-28T18:00:28.672150900
        }

        System.out.println(findListsToday.size());
        System.out.println(findListsToday.get(0).getMember().getUid());
    }

    @Test
    @Transactional
    @Rollback(false) // h2에서 확인하려면 자동 롤백 제거 필요
    public void deleteTest() throws Exception {
        // given
        Member member = new Member();
        member.setUid("1234");
        List<Task> tasks = new ArrayList<>();
        Task t1 = new Task();
        Task t2 = new Task();
        tasks.add(t1);
        tasks.add(t2);
        Lists lists = Lists.createLists(member, null, tasks);

        // when
        em.persist(member);
        listsRepository.save(lists); // em.persist
        Lists findLists = listsRepository.findOne(lists.getId());
        listsRepository.remove(findLists); //delete 쿼리 실행됨

        // then
        System.out.println(listsRepository.findOne(findLists.getId())); // null

=======
    TaskRepository taskRepository;
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
            TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);
            Task task = Task.createTask("조회 테스트입니다.", date_s, date_e, taskStatus);
            Task task2 = Task.createTask("조회 테스트입니다.2", date2_s, date2_e, taskStatus);
            tasks.add(task);
            tasks.add(task2);
        }
    }

    /**
     * findAllWithMemberTask, findByDateWithMemberTask, save, findByCurrent, findOneWithMemberTask, remove
     */
    @Test
    @Order(1)
    @Transactional
    @Rollback(value = false) // 롤백 취소
    public void 멤버의_일정저장과조회_한개와지정날짜() throws Exception {
        // given
        log.info("size : {}", tasks.size());
        Member member = Member.createMember("Lists 테스트", "닉네임테스트");
        em.persist(member);
        memberId = member.getId(); // 전역 기록

        // when
        List<Lists> listsList = new ArrayList<>();
        log.info("첫 taskId {}", tasks.get(0).getId());
        for (int i = 1; i <= 3; i++) {
            LocalDateTime listsDate = LocalDateTime.of(2023, 11, i, 1, 0);
            Lists lists = Lists.createLists(member, listsDate, tasks);
            listsRepository.save(lists); // persist
            listsList.add(lists);
        }
        listsId = listsList.get(0).getId();
        for (Task t : tasks) {
            log.info("taskId {}",t.getId());
//            em.persist(t.getTaskStatus()); // FK
//            em.persist(t);
            /**
             * 잠시 알게된 부분 정리
             * Lists.createLists 에서 tasks 내용이 수정이 되기 때문에 tasks 는 꼭 나중에 persist 적용!!
             * 만약 먼저 persist 적용된 상태에서 Lists.createLists 에서 tasks 내용 수정되면 더티체킹 발생하므로 update 쿼리 추가
             * 즉, insert + update 쿼리 전송되기 때문에 아래에서 persist 적용
             * => 이런 부분 Controller 에 있을수도 있으니 확인필요.
             */
        }

        // then
        for (int i = 1; i <= 3; i++) {
            LocalDateTime listsDate = LocalDateTime.of(2023, 11, i, 1, 0);
            Lists findLists = listsRepository.findByCurrent(member.getId(), listsDate); // flush
            Long listsId = findLists.getId();
            Lists findLists2 = listsRepository.findOneWithMemberTask(member.getId(), listsId); // flush
            Assertions.assertEquals(findLists.getId(), listsList.get(i - 1).getId());
            Assertions.assertEquals(findLists2.getId(), listsList.get(i - 1).getId());
        }
        log.info("마지막 taskId {}", tasks.get(0).getId());
    }

    @Test
    @Order(2)
    @Transactional
    public void 멤버의_일정조회_전체와날짜별() throws Exception {
        // given
        log.info("size : {}", tasks.size());
        // 2023-11-1~3 일정 조회위해 start, end 를 1~4
        LocalDateTime start = LocalDateTime.of(2023, 11, 1, 1, 0);
        LocalDateTime end = LocalDateTime.of(2023, 11, 4, 1, 0);

        // when
        List<Lists> findLists = listsRepository.findAllWithMemberTask(memberId);
        List<Lists> findLists2 = listsRepository.findByDateWithMemberTask(1L, start, end);

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
    @Transactional
    @Rollback(value = false) // db 적용 확인위해
    public void 멤버의_일정_삭제() throws Exception {
        // given
        Lists findLists = listsRepository.findOneWithMemberTask(memberId, listsId); // flush

        // when
        listsRepository.remove(findLists); // persist(delete)
        Lists lists3 = listsRepository.findOne(3L);
        log.info("delete 쿼리문 날라가는 시점 체크1");
        findLists = listsRepository.findOneWithMemberTask(memberId, listsId); // flush
        log.info("delete 쿼리문 날라가는 시점 체크2");

        // then
        Assertions.assertEquals(findLists, null);
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
    }
}