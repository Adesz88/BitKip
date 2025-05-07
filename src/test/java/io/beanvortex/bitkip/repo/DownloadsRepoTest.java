package io.beanvortex.bitkip.repo;

import io.beanvortex.bitkip.models.DownloadModel;
import io.beanvortex.bitkip.models.DownloadStatus;
import io.beanvortex.bitkip.utils.Defaults;
import io.beanvortex.bitkip.config.AppConfigs;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.beanvortex.bitkip.config.AppConfigs.downloadPath;
import static org.junit.jupiter.api.Assertions.*;

class DownloadsRepoTest {

    @Disabled
    @Test
    void updateDownloadLocation() {
        var name = "name";
        AppConfigs.log = LoggerFactory.getLogger("BitKip");
        var dm = DownloadModel.builder()
                .name(name).progress(0).downloaded(0).size(1254631)
                .uri(UUID.randomUUID().toString()).filePath(downloadPath + name)
                .chunks(8).addDate(LocalDateTime.now()).addToQueueDate(LocalDateTime.now())
                .lastTryDate(LocalDateTime.now()).completeDate(LocalDateTime.now())
                .queues(new CopyOnWriteArrayList<>(List.of(QueuesRepo.findByName(Defaults.ALL_DOWNLOADS_QUEUE, false))))
                .openAfterComplete(false).showCompleteDialog(false).downloadStatus(DownloadStatus.Paused)
                .resumable(true)
                .build();
        DownloadsRepo.insertDownload(dm);
        var dmId = dm.getId();
        var newPath = System.getProperty("user.home")
                + File.separator + "Desktop"
                + File.separator + "BitKip-test-folder-new" + File.separator;
        DownloadsRepo.updateDownloadLocation(newPath, dmId);
        var byId = DownloadsRepo.findById(dmId);

        assert byId.size() == 1;
        var fetchedDm = byId.get(0);

        assertEquals(newPath + name, fetchedDm.getFilePath());

        DownloadsRepo.deleteDownload(dm);
    }

    @Test
    public void testUpdateDownloadOpenAfterComplete() {
        MockedStatic<DatabaseHelper> mocked = Mockito.mockStatic(DatabaseHelper.class);
        DownloadModel downloadModel = new DownloadModel();

        downloadModel.setOpenAfterComplete(true);
        DownloadsRepo.updateDownloadOpenAfterComplete(downloadModel);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE downloads SET open_after_complete = 1 WHERE id = 0;\n", false));

        downloadModel.setOpenAfterComplete(false);
        DownloadsRepo.updateDownloadOpenAfterComplete(downloadModel);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE downloads SET open_after_complete = 0 WHERE id = 0;\n", false));

        mocked.close();
    }

    @Test
    public void testUpdateDownloadShowCompleteDialog() {
        MockedStatic<DatabaseHelper> mocked = Mockito.mockStatic(DatabaseHelper.class);
        DownloadModel downloadModel = new DownloadModel();

        downloadModel.setShowCompleteDialog(true);
        DownloadsRepo.updateDownloadShowCompleteDialog(downloadModel);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE downloads SET show_complete_dialog = 1 WHERE id = 0;\n", false));

        downloadModel.setShowCompleteDialog(false);
        DownloadsRepo.updateDownloadShowCompleteDialog(downloadModel);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE downloads SET show_complete_dialog = 0 WHERE id = 0;\n", false));

        mocked.close();
    }

    @Test
    public void testUpdateTableStatus() {
        MockedStatic<DatabaseHelper> mocked = Mockito.mockStatic(DatabaseHelper.class);
        DownloadModel downloadModel = new DownloadModel();

        downloadModel.setLastTryDate(null);
        downloadModel.setCompleteDate(null);
        DownloadsRepo.updateTableStatus(downloadModel);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE downloads SET progress = 0.000000, downloaded = 0, complete_date = NULL,\n" +
                "    last_try_date = NULL WHERE id = 0\n", false));

        LocalDateTime dateTime = LocalDate.of(2025, 5, 7).atStartOfDay();
        downloadModel.setLastTryDate(dateTime);
        downloadModel.setCompleteDate(dateTime);
        DownloadsRepo.updateTableStatus(downloadModel);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE downloads SET progress = 0.000000, downloaded = 0, complete_date = \"2025-05-07T00:00\",\n" +
                "    last_try_date = \"2025-05-07T00:00\" WHERE id = 0\n", false));

        mocked.close();
    }
}