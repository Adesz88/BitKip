package io.beanvortex.bitkip.utils;

import io.beanvortex.bitkip.config.AppConfigs;
import io.beanvortex.bitkip.models.DownloadModel;
import io.beanvortex.bitkip.models.DownloadStatus;
import io.beanvortex.bitkip.repo.QueuesRepo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.beanvortex.bitkip.config.AppConfigs.downloadPath;

import java.io.IOException;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class ValidationTests {
    @Test
    public void testValidateUri(){
        Assertions.assertTrue(Validations.validateUri("http://www.google.com"));
        Assertions.assertTrue(Validations.validateUri("https://www.google.com"));
        Assertions.assertTrue(Validations.validateUri("ftp://www.google.com"));
        Assertions.assertFalse(Validations.validateUri("test://www.google.com"));
    }

    @Test
    public void testFixUriChars(){
        Assertions.assertEquals(Validations.fixURIChars("{}[]`\" <>"), "%7b%7d%5b%5d%60%22%20%3c%3e");
    }

    @Test
    public void testMaxChunks(){
        int cpu = Runtime.getRuntime().availableProcessors();
        Assertions.assertEquals(0, Validations.maxChunks(1_999_999));
        Assertions.assertEquals(cpu < 10 ? cpu*2 : cpu , Validations.maxChunks(2_000_000));
    }

    @Test
    @SneakyThrows
    public void testFillNotFetchedData() {
        DownloadModel dm = DownloadModel.builder()
                .name("teszt").progress(0).downloaded(0).size(1254631)
                .uri(UUID.randomUUID().toString()).filePath(downloadPath + "test")
                .chunks(8).addDate(LocalDateTime.now()).addToQueueDate(LocalDateTime.now())
                .lastTryDate(LocalDateTime.now()).completeDate(LocalDateTime.now())
                .queues(new CopyOnWriteArrayList<>(List.of(QueuesRepo.findByName(Defaults.ALL_DOWNLOADS_QUEUE, false))))
                .openAfterComplete(false).showCompleteDialog(false).downloadStatus(DownloadStatus.Paused)
                .resumable(true)
                .build();
        DownloadModel original = dm;
        Validations.fillNotFetchedData(dm);
        Assertions.assertEquals(original, dm);
    }

    @Test
    public void testQueueDoesNotExist() {
        AppConfigs.initLogger();
        
        
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> DownloadModel.builder()
                .name("teszt").progress(0).downloaded(0).size(1254631)
                .uri(UUID.randomUUID().toString()).filePath(downloadPath + "test")
                .chunks(8).addDate(LocalDateTime.now()).addToQueueDate(LocalDateTime.now())
                .lastTryDate(LocalDateTime.now()).completeDate(LocalDateTime.now())
                .queues(new CopyOnWriteArrayList<>(List.of(QueuesRepo.findByName("gasgvas", false))))
                .openAfterComplete(false).showCompleteDialog(false).downloadStatus(DownloadStatus.Paused)
                .resumable(true)
                .build());
        String errorMessage = exception.getMessage();
        String expectedMessage = "Queue does not exist";
        
        Assertions.assertTrue(expectedMessage.equals(errorMessage));
    }
}
