package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.DownloadModel;
import io.beanvortex.bitkip.models.QueueModel;
import io.beanvortex.bitkip.models.ScheduleModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

public class QueueModelTests {
    @Test
    public void testQueueModel() {
        QueueModel queueModel = new QueueModel();
        Assertions.assertNotNull(queueModel);
        Assertions.assertNull(queueModel.getName());

        queueModel = new QueueModel("Teszt", true);
        Assertions.assertNotNull(queueModel);
        Assertions.assertNotNull(queueModel.getName());
        Assertions.assertEquals("Teszt", queueModel.getName());
        Assertions.assertTrue(queueModel.isCanAddDownload());

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
        Assertions.assertNotNull(queueModel2);
        Assertions.assertNotNull(queueModel2.getName());
        Assertions.assertEquals("TesztKetto", queueModel2.getName());
        Assertions.assertTrue(queueModel2.isCanAddDownload());
        Assertions.assertEquals(0, queueModel2.getDownloads().size());
        Assertions.assertNotNull(queueModel2.getSchedule());
        Assertions.assertTrue(queueModel2.hasFolder());
        Assertions.assertEquals("TesztKetto", queueModel2.toString());
        Assertions.assertEquals("QueueModel{" +
                "id=" + "1" +
                ", name='" + "TesztKetto" + '\'' +
                ", editable=" + "true" +
                ", canAddDownload=" + "true" +
                ", hasFolder=" + "true" +
                ", downloadFromTop=" + "false" +
                ", speed='" + "1" + '\'' +
                ", simultaneouslyDownload=" + "2" +
                ", schedule=" + "ScheduleModel(id=0, enabled=false, startTime=null, onceDownload=false, startDate=null, days=null, stopTimeEnabled=false, stopTime=null, turnOffEnabled=false, turnOffMode=null, queueId=0, startScheduler=null, stopScheduler=null)" +
                '}', queueModel2.toStringModel()
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
