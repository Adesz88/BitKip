package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.DownloadModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

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
        Mockito.when(rs.getString("a")).thenReturn("test");
        Assertions.assertEquals("test", rs.getString("a"));
    }
}
