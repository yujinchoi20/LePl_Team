package team.lepl_team.Repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.List.Lists;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class ListsRepositoryTest {

    @Autowired ListsRepository listsRepository;

    @Test
    @Rollback(false)
    public void 일정추가() throws Exception {
        //given
        Lists lists = new Lists();
        lists.setDate(LocalDate.now());

        //when
        listsRepository.save(lists);
        Lists findOne = listsRepository.findOne(lists.getId());

        //then
        Assertions.assertThat(lists).isEqualTo(findOne);
    }

    @Test
//    @Rollback(false)
    public void 일정조회() throws Exception {
        //given
        Lists lists = new Lists();
        lists.setDate(LocalDate.now());

        listsRepository.save(lists);

        //when
        Lists findOne = listsRepository.findOne(lists.getId());

        //then
        Assertions.assertThat(lists.getId()).isEqualTo(findOne.getId());
    }

    @Test
    public void 전제_일정조회() throws Exception {
        //given
        Lists lists1 = new Lists();
        lists1.setDate(LocalDate.now());
        lists1.setCount(5);

        Lists lists2 = new Lists();
        lists2.setDate(LocalDate.now());
        lists2.setCount(10);

        listsRepository.save(lists1);
        listsRepository.save(lists2);

        //when
        List<Lists> findAll = listsRepository.findAll();

        //then
        Assertions.assertThat(2).isEqualTo(findAll.size());
    }
}