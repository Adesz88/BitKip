package io.beanvortex.bitkip.task;

import io.beanvortex.bitkip.config.AppConfigs;
import io.beanvortex.bitkip.models.DownloadModel;
import java.nio.file.NoSuchFileException;
import javafx.application.Platform;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class SpecialDownloadTaskTest {

    @Test
    @SneakyThrows
    public void fail_ifFilePathIsNull() {
        //arrange / given
        DownloadModel downloadModel = new DownloadModel();
        //act / when
        //assert / then
        SpecialDownloadTask special = new SpecialDownloadTask(downloadModel);
        Assertions.assertThrows(NullPointerException.class, special::call);
    }

    @Test
    @SneakyThrows
    public void fail_ifFileNotExist() {
        //arrange / given
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setFilePath("test/test.txt");
        //act / when
        //assert / then
        SpecialDownloadTask special = new SpecialDownloadTask(downloadModel);
        Assertions.assertThrows(NullPointerException.class, special::call);
    }

    @Test
    @SneakyThrows
    public void fail_ifUriIsNotAbsolute() {
        //arrange / given
        new File("test/.temp/null#0").createNewFile();
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setFilePath("test/test.txt");
        downloadModel.setUri("teszt");
        //act / when
        //assert / then
        SpecialDownloadTask special = new SpecialDownloadTask(downloadModel);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, special::call);

        String expectedMessage = "URI is not absolute";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    @SneakyThrows
    public void fail_ifToolkitNotInitialized()
    {
        //arrange / given
        new File("test/.temp/null#0").createNewFile();
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setFilePath("test/test.txt");
        downloadModel.setUri("C:/Users/david/IdeaProjects/BitKip/src/main/resources/io/beanvortex/bitkip/icons/drag-and-drop-dark.png");
        //act / when
        //assert / then
        SpecialDownloadTask special = new SpecialDownloadTask(downloadModel);
        Exception exception = Assertions.assertThrows(IllegalStateException.class, special::call);
        
        String expectedMessage = "Toolkit not initialized";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }
    
    @Test
    @SneakyThrows
    public void fail_ifFileDoesNotExist()
    {
        try
        {
            Platform.startup(() ->
            {});
        }
        catch (IllegalStateException e)
        {
            // After the second start it will throw an exception
        }
        //arrange / given
        new File("test/.temp/null#0").createNewFile();
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setFilePath("test/test.txt");
        downloadModel.setUri("C:/Users/david/IdeaProjects/BitKip/src/main/resources/io/beanvortex/bitkip/icons/drag-and-drop-dark.png");
        //act / when
        //assert / then
        SpecialDownloadTask special = new SpecialDownloadTask(downloadModel);
        Exception exception = Assertions.assertThrows(NoSuchFileException.class, special::call);
        
        String expectedMessage = "test\\test.txt";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
        
    }
    
    @Test
    @SneakyThrows
    public void fail_ifTest()
    {
        Platform.startup(() -> {});
        //arrange / given
        new File("test/.temp/null#0").createNewFile();
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setFilePath("test/valid.txt");
        downloadModel.setUri("C:/Users/david/IdeaProjects/BitKip/src/main/resources/io/beanvortex/bitkip/icons/drag-and-drop-dark.png");
        //act / when
        //assert / then
        SpecialDownloadTask special = new SpecialDownloadTask(downloadModel);
        Long fileSize = special.call();
        Assertions.assertTrue(fileSize > 0);
        Assertions.assertEquals(21, fileSize); //string length
    }

    @BeforeEach
    void beforeEach() {
        AppConfigs.initLogger();
    }

    @AfterEach
    void cleanFiles(){
        new File("test/.temp").delete();
    }
}
