package com.lepl.domain.character;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class ExpTest {

    @Test
    public void 경험치_업데이트() throws Exception {
        // given
        Exp expTask = Exp.createExp(0L,0L,1L);
        Exp expTimer = Exp.createExp(0L,0L,1L);
        Exp expTaskNTimer = Exp.createExp(0L,0L,1L);

        // when
        expTask.updateExp(2L, 0L);
        expTimer.updateExp(0L, 4L);
        expTaskNTimer.updateExp(5L, 0L);
        expTaskNTimer.updateExp(0L, 6L);

        // then
        Assertions.assertEquals(expTask.getExpValue(), 2);
        Assertions.assertEquals(expTimer.getExpValue(), 4);
        Assertions.assertEquals(expTaskNTimer.getExpAll(), 5 + 6); // 누적 경험치
        Assertions.assertEquals(expTaskNTimer.getExpValue(), 1); // 현재 경험치 (11-10=1)
        Assertions.assertEquals(expTask.getLevel(), 1);
        Assertions.assertEquals(expTaskNTimer.getLevel(), 2);
    }
}