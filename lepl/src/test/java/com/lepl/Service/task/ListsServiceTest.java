package com.lepl.Service.task;

import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
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
@Transactional
//@Rollback(false) // db 확인위함
public class ListsServiceTest {
    @Autowired
    ListsService listsService;
    @Autowired
    EntityManager em;

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
    }
}