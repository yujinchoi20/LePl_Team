package com.lepl.domain.task;

import com.lepl.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lists {
    @Id
    @GeneratedValue
    @Column(name = "lists_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK
    private Member member;

    private LocalDateTime listsDate;
    private Long timerAllUseTime = 0L; // 타이머총사용시간 -> 반환때는 시:분:초로!
    private Long curTime = 0L; // 사용시간(계산용) -> 반환 절대안함

    // CascadeType.REMOVE 를 해줘야 고아객체가 안생기게 되며, Lists 삭제도 정상적으로 가능
    @OneToMany(mappedBy = "lists", cascade = CascadeType.REMOVE) // 양방향
    private List<Task> tasks = new ArrayList<>();

    /**
     * 연관관계 편의메서드 => 코드 감소
     * 즉, 연관관계 있는 엔티티끼리 유용한 메서드를 작성하면 됨
     */
    public void addTask(Task task) {
        task.setLists(this); // Task(엔티티)에 Lists(엔티티)참조
        this.tasks.add(task); // Lists(엔티티)의 List<Task>에 Task(엔티티)추가
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimerAllUseTime(Long timerAllUseTime) {
        this.timerAllUseTime = timerAllUseTime;
    }

    /**
     * 생성 편의메서드 => 수많은 정보를 한번에
     * 즉, 수많은 코드 작성을 또 줄여줌 & localDateTime null 일때 처리 등등
     */
    public static Lists createLists(Member member, LocalDateTime listsDate, List<Task> tasks) {
        Lists lists = new Lists();
        lists.setMember(member);
        // null 이면 오늘날짜
        if (listsDate == null) lists.listsDate = LocalDateTime.now();
        else lists.listsDate = listsDate;
        // null 이면 바로 pass
        for (Task task : tasks) {
            // 날짜 비교 함수
            if (!compareDate(task, lists.getListsDate())) continue;
            lists.addTask(task); // addTask로 넣어줘야 task.setLists(this); 적용
        }
        return lists;
    }

    /**
     * 비지니스 로직 => 엔티티내에서 가능한 비지니스 로직은 작성 권장(객체지향적)
     * 이건 작성할게 생기면 작성.
     */
    private static boolean compareDate(Task task, LocalDateTime listsDate) {
        // 년,월,일 만 비교하면 충분 하므로 Time 은 비교X
        LocalDate taskDay = task.getStartTime().toLocalDate();
        LocalDate listsDay = listsDate.toLocalDate();
        if (taskDay.compareTo(listsDay) == 0) { // 동일시 0
            return true;
        }
        return false;
    }

    public Lists updateTime(Long timerAllUseTime, Long curTime) {
        this.timerAllUseTime = timerAllUseTime;
        this.curTime = curTime;
        return this;
    }

    /**
     * 조회 편의메서드
     * 리스트(하루단위)내의 전체 일정 개수 조회 => 필요하면 작성
     */

}