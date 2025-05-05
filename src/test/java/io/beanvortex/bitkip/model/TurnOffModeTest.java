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

    @Test
    public void testGetValue() {
        var mode = TurnOffMode.getValue(null);
        Assertions.assertEquals(TurnOffMode.NOTHING, mode);

        mode = TurnOffMode.getValue("");
        Assertions.assertEquals(TurnOffMode.NOTHING, mode);

        mode = TurnOffMode.getValue("null");
        Assertions.assertEquals(TurnOffMode.NOTHING, mode);

        mode = TurnOffMode.getValue("SLEEP");
        Assertions.assertEquals(TurnOffMode.SLEEP, mode);
    }
}
