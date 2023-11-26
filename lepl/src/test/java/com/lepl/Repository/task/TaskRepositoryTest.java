package com.lepl.Repository.task;

<<<<<<< HEAD
import com.lepl.Repository.character.CharacterRepository;
=======
import com.lepl.domain.member.Member;
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import jakarta.persistence.EntityManager;
<<<<<<< HEAD
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
=======
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.time.Month;
import java.util.List;


// 현재 메모리에서 테스트하기 때문에 h2 DB에 적용을 보려면 main 함수에서!!
@SpringBootTest
=======
import java.util.ArrayList;
import java.util.List;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Slf4j
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
public class TaskRepositoryTest {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
<<<<<<< HEAD
    ListsRepository listsRepository;
    @Autowired
    EntityManager em;
    @Autowired
    ExpRepository expRepository;
    @Autowired
    CharacterRepository characterRepository;

    @Test
    @Transactional // 자동 롤백
    //@Rollback(false) // deleteTask() 테스트 위해 잠시 롤백 제거
    public void save_find_test() throws Exception {
        // given
        Lists lists = listsRepository.findOne(1l);
        //TaskStatus taskStatus = new TaskStatus();
        Task task1 = new Task();

        task1.setContent("test1");
        task1.setStartTime(LocalDateTime.of(2023, Month.OCTOBER, 12, 18, 00, 00));
        task1.setEndTime(LocalDateTime.of(2023, Month.OCTOBER, 12, 20, 00, 00));
        task1.setLists(lists);

        LocalDateTime start = task1.getStartTime();
        LocalDateTime end = task1.getEndTime();
        LocalDateTime remain = end.minusHours(start.getHour()).minusMinutes(start.getMinute()); //모든 일정이 하루안에 끝난다고 가정
        System.out.println("REMAIN = " + (remain.getHour()*60 + remain.getMinute()));

        // when
        taskRepository.save(task1);

        Task getTask = taskRepository.findOne(task1.getId());
        List<Task> list = taskRepository.findAll();

        // then
        Assertions.assertEquals(task1, getTask); // 정상
        if(!list.isEmpty()){
            for(int i = 0 ; i<list.size();i++){
                System.out.println(list.get(i).getContent()); // test1, test2
            }
        }
    }

    @Test
    @Transactional // 자동 롤백
    @Rollback(false) // h2에서 확인하려면 자동 롤백 제거 필요
    public void deleteTest() throws Exception {
        // given
        Task getTask = null;
        
        // when
        List<Task> list = taskRepository.findAll(); // test1, test2
        if(!list.isEmpty()) getTask = list.get(0); // test1

        if(getTask != null) {
            System.out.println(getTask.getId()); // 1
            taskRepository.remove(getTask);
//            em.flush(); // 강제 flush
        }
        list = taskRepository.findAll(); // test2
        
        // then
        if(!list.isEmpty()) {
            for(Task t : list) System.out.println(t.getId()); // 2
=======
    EntityManager em;
    static Long taskId;

    /**
     * save, findOne, findAll, findOneWithMember, remove
     */
    @Test
    @Order(1)
    @Transactional // 자동 롤백
    @Rollback(false) // 삭제() 테스트 위해 잠시 롤백 제거
    public void 일정_저장과조회() throws Exception {
        // given
        Task task = Task.createTask("테스트입니다.", LocalDateTime.now(), LocalDateTime.now(), TaskStatus.createTaskStatus(false,false));

        // when
        taskRepository.save(task); // persist
        taskId = task.getId();
        Task findTask = taskRepository.findOne(task.getId()); // 캐시에서 가져오는
        List<Task> taskList = taskRepository.findAll(); // flush

        // then
        Assertions.assertEquals(task.getId(), findTask.getId());
        log.info("taskList size : {}", taskList.size());
        log.info("taskID : {}", taskId);
        for (Task t : taskList) {
            log.info(t.getContent());
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
        }
    }

    @Test
    @Transactional
<<<<<<< HEAD
    public void 경험치_업데이트() throws Exception {
        //Given
        Task task = new Task();
        TaskStatus taskStatus = new TaskStatus();
        Lists lists = new Lists();

        taskStatus.setCompletedStatus(false);
        taskStatus.setTimerOnOff(false);
        task.setContent("Exp Update");
        task.setStartTime(LocalDateTime.of(2023, Month.OCTOBER, 12, 18, 00, 00));
        task.setEndTime(LocalDateTime.of(2023, Month.OCTOBER, 12, 20, 00, 00));
        task.setLists(lists);
        task.setTaskStatus(taskStatus);

        taskRepository.save(task);

        //When
        taskStatus.setCompletedStatus(true);
        task.setTaskStatus(taskStatus);

        //Then

=======
    public void 멤버의_일정조회() throws Exception {
        // given
        Task task = Task.createTask("멤버 테스트", LocalDateTime.now(), LocalDateTime.now(), TaskStatus.createTaskStatus(false,false));
        Member member = Member.createMember("UID", "닉네임");
        em.persist(member); // id 위해(FK 오류 방지)
        Lists lists = Lists.createLists(member, LocalDateTime.now(), new ArrayList<>());
        em.persist(task); // id
        lists.addTask(task);
        member.addLists(lists);
        em.persist(lists);
        log.info("전");
        em.flush(); // insert
        log.info("후");

        // when
        Task findTask = taskRepository.findOneWithMember(member.getId(), task.getId()); // flush

        // then
        Assertions.assertEquals(task, findTask); // em.clear() 없으므로 주소 동일
        Assertions.assertEquals(task.getId(), findTask.getId());
        Assertions.assertEquals(findTask.getContent(), "멤버 테스트");
    }

    @Test
    @Order(2)
    @Transactional // 자동 롤백
    @Rollback(false) // db 확인용
    public void 일정_삭제() throws Exception {
        // given
        Task task = taskRepository.findOne(taskId); // 위에서 저장했던 Task 찾기

        // when
        taskRepository.remove(task); // persist(delete)
        em.flush(); // 강제 flush()
        em.clear();
        task = taskRepository.findOne(taskId);

        // then
        Assertions.assertEquals(task, null);
>>>>>>> 7226d7649c24000b0c9079fbcc5c898f155c56cf
    }
}