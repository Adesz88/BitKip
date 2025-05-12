package io.beanvortex.bitkip.task;

import io.beanvortex.bitkip.models.ScheduleModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleTaskTest {
    @Test
    public void testCalculateOnceInitialDelay() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ScheduleModel schedule = new ScheduleModel();
        schedule.setStartDate(LocalDate.of(2025, 5, 10));
        schedule.setStartTime(LocalTime.of(20, 44, 0));
        schedule.setStopTime(LocalTime.of(21, 0));
        Method calculateOnceInitialDelay = ScheduleTask.class.getDeclaredMethod("calculateOnceInitialDelay", boolean.class, ScheduleModel.class);
        calculateOnceInitialDelay.setAccessible(true);

        long delay = (long) calculateOnceInitialDelay.invoke(null,false, schedule);
        Assertions.assertEquals(0, delay);

        delay = (long) calculateOnceInitialDelay.invoke(null,true, schedule);
        Assertions.assertEquals(0, delay);

        schedule.setStartTime(LocalTime.of(20, 44));
        schedule.setStopTime(LocalTime.of(19, 0));
        delay = (long) calculateOnceInitialDelay.invoke(null,true, schedule);
        Assertions.assertEquals(0, delay);
    }

    @Test
    public void testCalculateDailyInitialDelay() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method calculateDailyInitialDelay = ScheduleTask.class.getDeclaredMethod("calculateDailyInitialDelay", LocalTime.class);
        calculateDailyInitialDelay.setAccessible(true);

        LocalTime time = LocalTime.now().plusHours(1);
        long delay = (long) calculateDailyInitialDelay.invoke(null,time);
        Assertions.assertNotEquals(0, delay);

        time = LocalTime.now().minusHours(1);
        delay = (long) calculateDailyInitialDelay.invoke(null,time);
        Assertions.assertNotEquals(0, delay);
    }
}
