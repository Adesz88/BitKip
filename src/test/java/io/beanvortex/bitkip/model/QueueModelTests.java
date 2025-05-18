package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.DownloadModel;
import io.beanvortex.bitkip.models.QueueModel;
import io.beanvortex.bitkip.models.ScheduleModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

public class QueueModelTests {
    @Test
    public void testQueueModel2() {
        QueueModel queueModel = new QueueModel("Teszt", true);
        QueueModel queueModel2 = new QueueModel(
                1,
                "TesztKetto",
                true,
                true,
                true,
                false,
                "1",
                2,
                new ScheduleModel(),
                new CopyOnWriteArrayList<DownloadModel>()
        );

        Assertions.assertEquals(queueModel2, queueModel2);
        Assertions.assertNotEquals(queueModel2, null);
        Assertions.assertNotEquals(queueModel2, "test");
        Assertions.assertNotEquals(queueModel2, queueModel);
        QueueModel queueModel3 = new QueueModel(
                1,
                "TesztKetto",
                true,
                true,
                true,
                false,
                "1",
                2,
                new ScheduleModel(),
                new CopyOnWriteArrayList<DownloadModel>()
        );
        Assertions.assertEquals(queueModel2, queueModel3);

        queueModel3 = new QueueModel(
                1,
                "TesztKetto",
                false,
                true,
                true,
                false,
                "1",
                2,
                new ScheduleModel(),
                new CopyOnWriteArrayList<DownloadModel>()
        );
        Assertions.assertNotEquals(queueModel2, queueModel3);

        queueModel3 = new QueueModel(
                1,
                "TesztKetto",
                true,
                false,
                true,
                false,
                "1",
                2,
                new ScheduleModel(),
                new CopyOnWriteArrayList<DownloadModel>()
        );
        Assertions.assertNotEquals(queueModel2, queueModel3);
    }
}
