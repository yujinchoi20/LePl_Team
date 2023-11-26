package com.lepl.domain.character.dto;

import lombok.Getter;

@Getter
public class CalTimeDto {
    Boolean timerOnOff = true;
    Boolean completed = false;
    Long pointTimer = 0L;
    Long pointTask = 0L;
    Long timerAllUseTime;
    Long curTime;
    Long remainTime;

    public CalTimeDto(Long timerAllUseTime, Long curTime) {
        this.timerAllUseTime = timerAllUseTime;
        this.curTime = curTime;
    }

    public void setRemainTime(Long remainTime) {
        this.remainTime = remainTime;
    }

    public void calTime() {
        if (this.remainTime <= 0) {
            this.completed = true; // 잔여시간 없으면 당연히 일정 완료상태
            this.remainTime = 0L; // 음수는 사용X -> 0으로 초기화
        }
        if (this.curTime / (60 * 60 * 1000) != 0) {
            Long expTime = this.curTime / (60 * 60 * 1000);
            this.curTime = this.curTime % (60 * 60 * 1000);
            this.pointTimer = expTime; // 경험치용 시간
        }
    }

}