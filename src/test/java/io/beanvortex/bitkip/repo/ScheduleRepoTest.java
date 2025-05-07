package io.beanvortex.bitkip.repo;

import io.beanvortex.bitkip.models.ScheduleModel;
import io.beanvortex.bitkip.models.TurnOffMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScheduleRepoTest {
    @Test
    public void testValidScheduleProperties() {
        ScheduleModel schedule = new ScheduleModel();
        LocalDate date = LocalDate.of(2025, 5, 7);
        LocalTime time = LocalTime.of(20, 15);
        Set<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.WEDNESDAY);

        schedule.setStartTime(time);
        schedule.setStartDate(date);
        schedule.setStopTime(time);
        schedule.setTurnOffMode(TurnOffMode.NOTHING);
        schedule.setDays(days);

        Map<String, String> properties = ScheduleRepo.validScheduleProperties(schedule);
        Assertions.assertEquals("\"20:15\"", properties.get("start_time"));
        Assertions.assertEquals("\"2025-05-07\"", properties.get("start_date"));
        Assertions.assertEquals("\"20:15\"", properties.get("stop_time"));
        Assertions.assertEquals("\"NOTHING\"", properties.get("turn_off_mode"));
        Assertions.assertEquals("\"WEDNESDAY\"", properties.get("days"));

        schedule.setStartTime(null);
        schedule.setStartDate(null);
        schedule.setStopTime(null);
        schedule.setTurnOffMode(null);
        schedule.setDays(null);

        properties = ScheduleRepo.validScheduleProperties(schedule);
        Assertions.assertEquals("NULL", properties.get("start_time"));
        Assertions.assertEquals("NULL", properties.get("start_date"));
        Assertions.assertEquals("NULL", properties.get("stop_time"));
        Assertions.assertEquals("NULL", properties.get("turn_off_mode"));

        String daysString = properties.get("days");
        Assertions.assertTrue(daysString.contains("MONDAY"));
        Assertions.assertTrue(daysString.contains("TUESDAY"));
        Assertions.assertTrue(daysString.contains("WEDNESDAY"));
        Assertions.assertTrue(daysString.contains("THURSDAY"));
        Assertions.assertTrue(daysString.contains("FRIDAY"));
        Assertions.assertTrue(daysString.contains("SATURDAY"));
        Assertions.assertTrue(daysString.contains("SUNDAY"));
    }
}
