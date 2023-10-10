package com.lepl.domain.character;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Getter @Setter
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
    public Exp updateExp(Long pointTimer, Long pointTask) {
        if(pointTask > 0 && pointTodayTask < 12) {
            Long checkMax = pointTodayTask + pointTask;

            if(checkMax > 12) { //일일 최대 획득 경험치를 초과할 경우
                pointTask = 12 - pointTodayTask;
                pointTodayTask = 12L;
            } else {
                pointTodayTask += pointTask;
            }

            this.expAll += pointTask;
            this.expValue += pointTask;

            while(this.expValue >= reqExp) {
                this.level += 1; //레벨업
                this.expValue -= reqExp;
                this.reqExp = (long)(Math.pow(this.level-1, 2) * 1.5); //레벨업에 따라 필요경험치 증가
            }
        }

        if(pointTimer > 0 && pointTodayTimer < 12) {
            Long checkMax = pointTodayTimer + pointTimer;

            if(checkMax > 12) { //일일 최대 획득 타이머 경험치 초과할 경우
                pointTimer = 12 - pointTodayTimer;
                pointTodayTimer = 12L;
            } else {
                pointTodayTimer += pointTimer;
            }

            this.expAll += pointTimer;
            this.expValue += pointTimer;

            while(this.expValue >= reqExp) {
                this.level += 1;//레벨업
                this.expValue -= reqExp;
                this.reqExp = (long)(Math.pow(this.level-1, 2) * 1.5); //레벨업에 따라 필요경험치 증가
            }
        }

        return this;
    }
}
