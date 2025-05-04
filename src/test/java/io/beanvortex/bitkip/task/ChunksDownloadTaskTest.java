package io.beanvortex.bitkip.task;

import io.beanvortex.bitkip.config.AppConfigs;
import io.beanvortex.bitkip.exceptions.DeniedException;
import io.beanvortex.bitkip.models.DownloadModel;
import io.beanvortex.bitkip.models.FileType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;

public class ChunksDownloadTaskTest {

    private ChunksDownloadTask task;

    @Test
    @SneakyThrows
    public void test() {
        //arrange / given
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setChunks(1);
        Long speedLimit = 2000L, byteLimit = 1000000L;
        task = new ChunksDownloadTask(downloadModel, speedLimit, byteLimit);

        //act / when
        Long result = task.call();

        //assert / then
        Assertions.assertEquals(0L, result);
    }

    @Test
    @SneakyThrows
    public void fail_ifChunkIsZero() {
        //arrange / given
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setChunks(0);
        long speedLimit = 2000L, byteLimit = 1000000L;

        //act / when
        //assert / then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new ChunksDownloadTask(downloadModel, speedLimit, byteLimit));

        String expectedMessage = "To download file in chunks, chunks must not be 0";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @SneakyThrows
    public void fail_ifByteLimitIsZero() {
        //arrange / given
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setChunks(1);
        long speedLimit = 2000L;

        //act / when
        //assert / then
        Exception exception = Assertions.assertThrows(DeniedException.class, () -> new ChunksDownloadTask(downloadModel, speedLimit, 0l));

        String expectedMessage = "File did not download due to 0 bytes chosen to download";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @SneakyThrows
    public void shouldReturnZero_whenFileExists() {
        //arrange / given
        boolean file = new File("test/.temp/null#0").createNewFile();
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setChunks(1);
        downloadModel.setFilePath("test/test.txt");
        Long speedLimit = 2000L, byteLimit = 1000000L;
        task = new ChunksDownloadTask(downloadModel, speedLimit, byteLimit);
        task.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        //act / when
        Long result = task.call();

        //assert / then
        Assertions.assertEquals(0L, result);
    }

    @Test
    @SneakyThrows
    public void shouldReturnZero_whenFileDoesNotExist() {
        //arrange / given
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setChunks(1);
        downloadModel.setFilePath("test/asd.txt");
        downloadModel.setSize(2000L);
        Long speedLimit = 2000L, byteLimit = 1000000L;
        task = new ChunksDownloadTask(downloadModel, speedLimit, byteLimit);
        task.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        //act / when
        Long result = task.call();

        //assert / then
        Assertions.assertEquals(0L, result);
    }


    @BeforeEach
    public void setUp() {
        AppConfigs.initLogger();
    }

    @AfterEach
    void cleanFiles(){
        new File("test/.temp").delete();
    }
}
