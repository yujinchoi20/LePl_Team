package com.lepl.domain.task;

import com.lepl.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Lists {
    @Id @GeneratedValue
    @Column(name = "lists_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK
    private Member member;

    private LocalDateTime listsDate;

    // CascadeType.REMOVE 를 해줘야 고아객체가 안생기게 되며, Lists 삭제도 정상적으로 가능
    @OneToMany(mappedBy = "lists", cascade = CascadeType.REMOVE) // 양방향
    private List<Task> tasks = new ArrayList<>();

    /**
     * 연관관계 편의 메서드 => 코드 감소
     * 즉, 연관관계 있는 엔티티끼리 유용한 메서드를 작성하면 됨
     */
    public void addTask(Task task) {
        task.setLists(this); // Task(엔티티)에 Lists(엔티티)참조
        this.tasks.add(task); // Lists(엔티티)의 List<Task>에 Task(엔티티)추가
    }
    public void setMember(Member member) {
        this.member = member;
        member.getLists().add(this); // 기존 setter 로는 이부분 로직이 없음
    }

    /**
     * 생성 메서드 => 수많은 정보를 한번에
     * 즉, 수많은 코드 작성을 또 줄여줌 & localDateTime null 일때 처리 등등
     */
    public static Lists createLists(Member member, LocalDateTime localDateTime, List<Task> tasks) {
        Lists lists = new Lists();
        lists.setMember(member);
        // null이면 바로 pass 될것임
        for(Task task : tasks) {
            lists.addTask(task); // addTask로 넣어줘야 task.setLists(this); 적용
        }
        // null이면 오늘날짜
        if(localDateTime==null) lists.setListsDate(LocalDateTime.now());
        else lists.setListsDate(localDateTime);
        return lists;
    }

    /**
     * 비지니스 로직 => 엔티티내에서 가능한 비지니스 로직은 작성 권장(객체지향적)
     * 이건 작성할게 생기면 작성.
     */

    /**
     * 조회 로직
     * 리스트(하루단위)내의 전체 일정 개수 조회
     */
    public int getTaskCount() {
        return this.tasks.size();
    }
}
