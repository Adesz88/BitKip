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

        queueModel = new QueueModel(
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
        Assertions.assertNotNull(queueModel);
        Assertions.assertNotNull(queueModel.getName());
        Assertions.assertEquals("TesztKetto", queueModel.getName());
        Assertions.assertTrue(queueModel.isCanAddDownload());
        Assertions.assertEquals(0, queueModel.getDownloads().size());
        Assertions.assertNotNull(queueModel.getSchedule());
        Assertions.assertTrue(queueModel.hasFolder());
        Assertions.assertEquals("TesztKetto", queueModel.toString());
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
                '}', queueModel.toStringModel());
    }
}
