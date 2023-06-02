package team.lepl_team.Service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.List.Lists;
import team.lepl_team.Repository.ListsRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ListsServiceTest {

    @Autowired
    ListsRepository listsRepository;
    @Autowired
    ListsService listsService;

    @Test
    public void 일정추가() throws Exception {
        //given
        Lists lists = new Lists();
        lists.setDate(LocalDate.now());
        lists.setCount(3);

        //when
        listsService.enroll(lists);
        Lists findOne = listsRepository.findOne(lists.getId());

        //then
        Assertions.assertThat(lists).isEqualTo(findOne);
    }

    @Test
    public void 중복_일정() throws Exception {
        //given
        Lists lists1 = new Lists();
        lists1.setDate(LocalDate.now());
        lists1.setCount(3);

        Lists lists2 = new Lists();
        lists2.setDate(LocalDate.now());
        lists2.setCount(2);

        //when
        listsService.enroll(lists1);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> listsService.enroll(lists2));
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 일정(날짜)입니다.");
    }

    @Test
    public void 일정조회() throws Exception {
        //given
        Lists lists = new Lists();
        lists.setDate(LocalDate.now());
        lists.setCount(3);

        listsService.enroll(lists);

        //when
        Lists findOne = listsService.findOne(lists.getId());

        //then
        Assertions.assertThat(lists.getCount()).isEqualTo(findOne.getCount());
    }

    @Test
    public void 전체_일정조회() throws Exception {
        //given
        Lists lists = new Lists();
        lists.setCount(3);

        Lists lists2 = new Lists();
        lists2.setCount(5);

        listsService.enroll(lists);
        listsService.enroll(lists2);

        //when
        List<Lists> findAll = listsService.findAll();

        //then
        for(Lists l : findAll) {
            System.out.println("NUM : " + l.getCount());
        }
    }
}