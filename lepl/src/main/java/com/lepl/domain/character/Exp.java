package com.lepl.domain.character;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter @Setter
@Slf4j
public class Exp {
    @Id @GeneratedValue
    @Column(name = "exp_id")
    private Long id;

    private Long expAll = 0L; //누적 경험치
    private Long expValue = 0L; //현재 경험치 -> 상점에서 사용하고 남은 경험치
    private Long reqExp = 1L; //필요 경험치, 초기값 1
    private Long level = 1L; //누적 경험치를 가지고 공식에 따라 표현한 레벨

    private Long pointTodayTimer = 0L; //일일 타이머 획득 경험치
    private Long pointTodayTask = 0L; //일일 일정 획득 경험치

    //매일 pointTodayTimer, pointTodayTask 는 0L 으로 초기화!

    /*
    * 비즈니스 편의 메서드
    */
    //== 비지니스 편의 메서드 ==//
    public Exp updateExp(Long pointTask, Long pointTimer) {
        if(pointTask>0 && pointTodayTask < 12) {
            Long checkMax = pointTodayTask+pointTask;
            if(checkMax > 12) { // 최대 일일 경험치 허용량 12
                pointTask = 12-pointTodayTask;
                pointTodayTask = 12l;
            }
            else pointTodayTask += pointTask;

            this.expAll += pointTask;
            this.expValue += pointTask;
            log.debug("처음 task : expValue {}, reqExp {}, level {}",expValue,reqExp,level);
            while(this.expValue >= reqExp) {
                this.level++;
                this.expValue = this.expValue-reqExp;
                this.reqExp = (long)(Math.pow(this.level,1.2)+10); // 필요경험치 update
                log.debug("expValue {}, reqExp {}, level {}",expValue,reqExp,level);
            }
        }else if(pointTodayTask>=12) { // 디버깅용
            log.debug("exp pointTodayTask>=12 즉, 일일경험치 최대치 흭득상태");
        }
        if(pointTimer>0 && pointTodayTimer < 12){
            Long checkMax = pointTodayTimer+pointTimer;
            if(checkMax > 12) { // 최대 일일 경험치 허용량 12
                pointTimer = 12-pointTodayTimer;
                pointTodayTimer = 12l;
            }else pointTodayTimer+=pointTimer;

            this.expAll += pointTimer;
            this.expValue += pointTimer;
            log.debug("처음 timer : expValue {}, reqExp {}, level {}",expValue,reqExp,level);
            while(this.expValue >= reqExp) {
                this.level++;
                this.expValue = this.expValue-reqExp;
                this.reqExp = (long)(Math.pow(this.level,1.2)+10); // 필요경험치 update
                log.debug("expValue {}, reqExp {}, level {}",expValue,reqExp,level);
            }
        }else if(pointTodayTimer>=12) { // 디버깅용
            log.debug("exp pointTodayTimer>=12 즉, 일일경험치 최대치 흭득상태");
        }

        return this;
    }
}
