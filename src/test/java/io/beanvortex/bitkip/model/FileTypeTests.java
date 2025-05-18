package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.config.AppConfigs;
import io.beanvortex.bitkip.models.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileTypeTests {
    @Test
    public void enumTestForOther(){
        AppConfigs.initPaths();

        FileType fileType = FileType.determineFileType("test");
        Assertions.assertEquals(FileType.OTHER, fileType);
    }
}
