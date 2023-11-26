package com.lepl.domain.character;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Exp {
    @Id
    @GeneratedValue
    @Column(name = "exp_id")
    private Long id;

    private Long expAll = 0L; // 누적
    private Long expValue = 0L; // 현재
    private Long reqExp = 10L; // 필요경험치(초기값 10)
    private Long level = 1L;

    private Long pointTodayTimer = 0L; // 일일 타이머 경험치량
    private Long pointTodayTask = 0L; // 일일 일정 경험치량

    /**
     * 생성 편의 메서드
     */
    public static Exp createExp(Long expAll, Long expValue, Long level) {
        Exp exp = new Exp();
        exp.expAll = expAll;
        exp.expValue = expValue;
        exp.level = level;
        return exp;
    }

    /*
     * 비즈니스 편의 메서드
     * 1. updateExp: 경험치 업데이트
     * 2. checkExp: 일일 최대 경험치 체크
     * 3. levelUp: 레벨업 확인 및 필요 경험치 업데이트
     */
    public Exp updateExp(Long pointTask, Long pointTimer) {
        if (pointTask > 0 && pointTodayTask < 12) {
            Long checkMax = pointTodayTask + pointTask;
            pointTodayTask = checkExp(checkMax, pointTask, pointTodayTask);
            log.debug("처음 task : expValue {}, reqExp {}, level {}", expValue, reqExp, level);
            levelUp(); //레벨업 확인 및 필요 경험치 업데이트
        } else if (pointTodayTask >= 12) { // 디버깅용
            log.debug("exp pointTodayTask>=12 즉, 일일경험치 최대치 흭득상태");
        }

        if (pointTimer > 0 && pointTodayTimer < 12) {
            Long checkMax = pointTodayTimer + pointTimer;
            pointTodayTimer = checkExp(checkMax, pointTimer, pointTodayTimer);
            log.debug("처음 timer : expValue {}, reqExp {}, level {}", expValue, reqExp, level);
            levelUp(); //레벨업 확인 및 필요 경험치 업데이트
        } else if (pointTodayTimer >= 12) { // 디버깅용
            log.debug("exp pointTodayTimer>=12 즉, 일일경험치 최대치 흭득상태");
        }

        return this;
    }

    public Long checkExp(Long checkMax, Long point, Long pointToday) {
        if (checkMax > 12) {
            point = 12 - pointToday;
            pointToday = 12l;
        } else {
            pointToday += point;
        }

        expAll += point;
        expValue += point;

        return pointToday;
    }

    public void levelUp() {
        while (this.expValue >= reqExp) {
            level++;
            expValue = expValue - reqExp;
            reqExp = (long) (Math.pow(level, 1.1) + 10); // 필요경험치 update
            log.debug("expValue {}, reqExp {}, level {}", expValue, reqExp, level);
        }
    }

}