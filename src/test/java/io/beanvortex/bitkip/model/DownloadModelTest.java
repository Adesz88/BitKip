package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.DownloadModel;
import io.beanvortex.bitkip.models.DownloadStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class DownloadModelTest {
    @Test
    public void testEquals() {
        DownloadModel downloadModel = new DownloadModel();
        Assertions.assertNotNull(downloadModel);

        downloadModel.setId(1);
        Assertions.assertEquals(1, downloadModel.getId());

        Assertions.assertEquals(downloadModel, downloadModel);
        Assertions.assertNotEquals(downloadModel, null);
        Assertions.assertNotEquals(downloadModel, "test");

        DownloadModel downloadModel2 = new DownloadModel();
        downloadModel2.setId(2);
        Assertions.assertNotEquals(downloadModel, downloadModel2);

        downloadModel2.setId(1);
        Assertions.assertEquals(downloadModel, downloadModel2);
    }

    @Test
    public void testGetLastTryDateString() {
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setLastTryDateString(null);
        Assertions.assertEquals("", downloadModel.getLastTryDateString());

        LocalDateTime dateTime = LocalDate.of(2025, Month.MAY, 4).atStartOfDay();
        downloadModel.setLastTryDate(dateTime);
        Assertions.assertEquals("2025/05/04 - 00:00:00", downloadModel.getLastTryDateString());
    }

    @Test
    public void testGeCompleteDateString() {
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setCompleteDate(null);
        Assertions.assertEquals("", downloadModel.getCompleteDateString());

        LocalDateTime dateTime = LocalDate.of(2025, Month.MAY, 4).atStartOfDay();
        downloadModel.setCompleteDate(dateTime);
        Assertions.assertEquals("2025/05/04 - 00:00:00", downloadModel.getCompleteDateString());
    }

    @Test
    public void testCreateDownload() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        //Mockito.when(rs.getFloat("progress")).thenReturn((42f));
        Mockito.when(rs.getString("add_date")).thenReturn("2025-05-04T10:15:30");
        Mockito.when(rs.getString("add_to_queue_date")).thenReturn("2025-05-04T10:15:30");
        Mockito.when(rs.getString("last_try_date")).thenReturn(null);
        Mockito.when(rs.getString("complete_date")).thenReturn(null);

        DownloadModel downloadModel = DownloadModel.createDownload(rs, false);
        Assertions.assertEquals("", downloadModel.getLastTryDateString());
        Assertions.assertEquals("", downloadModel.getCompleteDateString());
        Assertions.assertEquals(DownloadStatus.Paused, downloadModel.getDownloadStatus());
        Assertions.assertTrue(downloadModel.getQueues().isEmpty());

        Mockito.when(rs.getFloat("progress")).thenReturn((100f));
        Mockito.when(rs.getString("add_date")).thenReturn("2025-05-04T10:15:30");
        Mockito.when(rs.getString("add_to_queue_date")).thenReturn("2025-05-04T10:15:30");
        Mockito.when(rs.getString("last_try_date")).thenReturn("2025-05-04T10:15:30");
        Mockito.when(rs.getString("complete_date")).thenReturn("2025-05-04T10:15:30");
        Mockito.when(rs.getString("days")).thenReturn("MONDAY");

        downloadModel = DownloadModel.createDownload(rs, true);
        Assertions.assertEquals("2025/05/04 - 10:15:30", downloadModel.getLastTryDateString());
        Assertions.assertEquals("2025/05/04 - 10:15:30", downloadModel.getCompleteDateString());
        Assertions.assertEquals(DownloadStatus.Completed, downloadModel.getDownloadStatus());
        Assertions.assertFalse(downloadModel.getQueues().isEmpty());
    }
}
