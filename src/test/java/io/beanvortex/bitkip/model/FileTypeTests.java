package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.config.AppConfigs;
import io.beanvortex.bitkip.models.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileTypeTests {

    @Test
    public void enumTest(){
        AppConfigs.initPaths();
        FileType fileType = FileType.COMPRESSED;
        Assertions.assertEquals(AppConfigs.compressedPath, fileType.getPath());
        fileType = FileType.VIDEO;
        Assertions.assertEquals(AppConfigs.videosPath, fileType.getPath());
        fileType = FileType.PROGRAM;
        Assertions.assertEquals(AppConfigs.programsPath, fileType.getPath());
        fileType = FileType.MUSIC;
        Assertions.assertEquals(AppConfigs.musicPath, fileType.getPath());
        fileType = FileType.DOCUMENT;
        Assertions.assertEquals(AppConfigs.documentPath, fileType.getPath());
        fileType = FileType.OTHER;
        Assertions.assertEquals(AppConfigs.othersPath, fileType.getPath());
        fileType = FileType.QUEUES;
        Assertions.assertEquals(AppConfigs.queuesPath, fileType.getPath());
        Assertions.assertNotEquals(AppConfigs.compressedPath, fileType.getPath());
    }

    @Test
    public void enumTest2(){
        AppConfigs.initPaths();
        FileType fileType = FileType.determineFileType("teszt.zip");
        Assertions.assertEquals(FileType.COMPRESSED, fileType);
    }
}
