package com.lepl.Repository.task;

import com.lepl.domain.task.Task;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// 현재 메모리에서 테스트하기 때문에 h2 DB에 적용을 보려면 main 함수에서!!
@SpringBootTest
public class TaskRepositoryTest {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    EntityManager em;

    @Test
    @Transactional // 자동 롤백
    @Rollback(false) // deleteTask() 테스트 위해 잠시 롤백 제거
    public void save_find_test() throws Exception {
        // given
        Task task1 = new Task();
        task1.setContent("test1");
//        task1.setStartTime("10:30");
//        task1.setEndTime("11:00");
        Task task2 = new Task();
        task2.setContent("test2");
//        task2.setStartTime("10:30");
//        task2.setEndTime("11:00");
        System.out.println(task1.getId()); // null

        // when
        taskRepository.save(task1);
        taskRepository.save(task2);
//        em.flush(); // 강제 flush
        Task getTask = taskRepository.findOne(task1.getId());
        List<Task> list = taskRepository.findAll();

        // then
        Assertions.assertEquals(task1, getTask); // 정상
        if(!list.isEmpty()){
            for(int i = 0 ; i<list.size();i++){
                System.out.println(list.get(i).getContent()); // test1, test2ㅆ
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
        }
    }
}