package com.lepl.domain.task;

import com.lepl.domain.member.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class ListsTest {
    @Autowired
    EntityManager em;

    /**
     * setMember, addTask
     */
    @Test
    @Transactional
    public void 연관관계_편의함수_테스트() throws Exception {
        // given
        LocalDateTime testDate = LocalDateTime.of(2022, Month.APRIL, 23, 12, 30); // test 날짜

        Member member = new Member();
        Lists lists = new Lists();
        lists.setListsDate(testDate);
        Task t1 = new Task();
        Task t2 = new Task();

        // when
        lists.setMember(member);
        lists.addTask(t1); // 연관관계 편의 메서드
        lists.addTask(t2); // 연관관계 편의 메서드
        Long dbNotId = lists.getId(); // long이 아닌 Long덕분에 null타입 가질 수 있음
        em.persist(lists); // db insert log 보려고.
        Lists getLists = em.find(Lists.class, lists.getId());
        Long dbYesId = getLists.getId(); // db 적용후 생성된 id값 확인용

        // then
        System.out.println(member.getLists().get(0).getListsDate()); // 2022-04-23T12:30
        Assertions.assertEquals(lists.getId(), getLists.getId()); // exp:1, act:1
        Assertions.assertEquals(lists.getTasks().get(0).getId(),t1.getId()); // exp:2, act:2
        Assertions.assertEquals(lists.getTasks().get(1).getId(),t2.getId()); // exp:3, act:3
        Assertions.assertEquals(dbNotId, dbYesId); // exp:null, act:1 => 에러발생
    }

    /**
     * createLists
     */
    @Test
    @Transactional
    public void 생성메서드_테스트() throws Exception {
        // given
        LocalDateTime testDate = LocalDateTime.of(2022, Month.APRIL, 23, 12, 30); // test 날짜

        Member member = new Member();
        List<Task> tasks = new ArrayList<>();
        Task t1 = new Task();
        Task t2 = new Task();
        tasks.add(t1);
        tasks.add(t2);

        // when
        Lists lists = Lists.createLists(member, testDate, tasks);
        em.persist(lists);

        // then
        System.out.println(lists.getListsDate()); // 2022-04-23T12:30
        Assertions.assertEquals(lists.getTasks().get(0).getId(),t1.getId()); // exp:5, act:5
        Assertions.assertEquals(lists.getTasks().get(1).getId(),t2.getId()); // exp:6, act:6
    }

    /**
     * getTaskCount
     */
    @Test
    @Transactional
    public void 조회로직_테스트() throws Exception {
        // given
        LocalDateTime testDate = LocalDateTime.of(2022, Month.APRIL, 23, 12, 30); // test 날짜

        Member member = new Member();
        List<Task> tasks = new ArrayList<>();
        Task t1 = new Task();
        Task t2 = new Task();
        tasks.add(t1);
        tasks.add(t2);

        // when
        Lists lists = Lists.createLists(member, testDate, tasks);
        em.persist(lists);
        Integer totalTaskCount = lists.getTaskCount();

        // then
        System.out.println(totalTaskCount); // 2
    }
}