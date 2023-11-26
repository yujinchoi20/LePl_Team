package com.lepl.domain.character.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class CalTimeDtoTest {

    @Test
    public void 시간_계산() throws Exception {
        // given
        CalTimeDto calTimeDto = new CalTimeDto(0L, 60 * 60 * 1000*5L);
        calTimeDto.setRemainTime(0L);

        // when
        calTimeDto.calTime();

        // then
        log.info("completed : {}, timerOnOff : {}", calTimeDto.getCompleted(), calTimeDto.getTimerOnOff());
        log.info("curTime : {}, pointTimer : {}", calTimeDto.getCurTime(), calTimeDto.getPointTimer());
        Assertions.assertEquals(calTimeDto.getCompleted(), true);
        Assertions.assertEquals(calTimeDto.getPointTimer(), 5);
    }
}