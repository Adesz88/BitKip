package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.TurnOffMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TurnOffModeTest {
    @Test
    public void testTurnOffMode() {
        TurnOffMode turnOffMode = TurnOffMode.NOTHING;
        Assertions.assertEquals("NOTHING", turnOffMode.toString());
        turnOffMode = TurnOffMode.SLEEP;
        Assertions.assertEquals("SLEEP", turnOffMode.toString());
        turnOffMode = TurnOffMode.TURN_OFF;
        Assertions.assertEquals("TURN_OFF", turnOffMode.toString());
    }
}
