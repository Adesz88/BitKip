package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.ScheduleModel;
import io.beanvortex.bitkip.models.TurnOffMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ScheduleModelTest {
    @Test
    public void testEquals() {
        ScheduleModel scheduleModel = new ScheduleModel();
        Assertions.assertNotNull(scheduleModel);
        scheduleModel.setId(1);
        Assertions.assertEquals(1, scheduleModel.getId());

        Assertions.assertEquals(scheduleModel, scheduleModel);
        Assertions.assertNotEquals(scheduleModel, null);
        Assertions.assertNotEquals(scheduleModel, "test");

        ScheduleModel scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        Set<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.SUNDAY);
        scheduleModel2.setDays(days);
        scheduleModel.setDays(days);
        Assertions.assertEquals(scheduleModel, scheduleModel2);

        scheduleModel2.setId(2);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setEnabled(true);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setOnceDownload(true);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setStopTimeEnabled(true);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setOnceDownload(true);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setStopTimeEnabled(true);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setTurnOffEnabled(true);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setQueueId(2);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setStartTime(LocalTime.of(1, 1, 1));
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setStartDate(LocalDate.of(2025, 5, 4));
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setStopTime(LocalTime.of(1, 1, 1));
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        scheduleModel2.setDays(days);
        scheduleModel2.setTurnOffMode(TurnOffMode.TURN_OFF);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);

        scheduleModel2 = new ScheduleModel();
        scheduleModel2.setId(1);
        days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        scheduleModel2.setDays(days);
        Assertions.assertNotEquals(scheduleModel, scheduleModel2);
    }
}
