package io.beanvortex.bitkip.utils;


import io.beanvortex.bitkip.config.AppConfigs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.beanvortex.bitkip.config.AppConfigs.*;

class IOUtilsTest {

    @Test
    void moveAndDeletePreviousData() throws IOException {
        var path = System.getProperty("user.home")
                + File.separator + "Desktop"
                + File.separator + "BitKip-test-folder" + File.separator;
        var newPath = System.getProperty("user.home")
                + File.separator + "Desktop"
                + File.separator + "BitKip-test-folder-new" + File.separator;

        AppConfigs.log = LoggerFactory.getLogger("BitKip");

        IOUtils.mkdir(path);
        for (int i = 0; i < 10; i++) {
            var file = new File(path + "file" + i);
            if (!file.exists())
                file.createNewFile();
        }
        IOUtils.mkdir(path + "folder1");

        for (int i = 0; i < 10; i++) {
            var file = new File(path + "folder1" + File.separator + "file" + i);
            if (!file.exists())
                file.createNewFile();
        }

        IOUtils.moveAndDeletePreviousData(path, newPath);
        var prevFolder = new File(path);
        var prevFolderWithIn = new File(path + "folder1");
        var newFolder = new File(newPath);
        var newFolderWithIn = new File(newPath + "folder1");

        assert !prevFolder.exists();
        assert !prevFolderWithIn.exists();
        assert newFolder.exists();
        assert newFolderWithIn.exists();
        assert newFolder.listFiles().length == 11;
        assert newFolderWithIn.listFiles().length == 10;

        IOUtils.deleteFolderWithContent(newPath);
        assert !newFolder.exists();
        assert !newFolderWithIn.exists();
    }

    @Test
    public void testReadConfig() throws IOException {
        AppConfigs.log = LoggerFactory.getLogger("BitKip");
        var file = new File(dataPath + "config.cfg");
        if (!file.exists())
            file.createNewFile();

        var writer = new FileWriter(file);
        String saveLocation = "save_location=" + downloadPath + "\n";
        writer.append(saveLocation).append(
            """
            theme=light
            startup=false
            server_enabled=false
            port=9563
            trigger_turn_off_on_empty_queue=true
            show_complete_dialog=true
            continue_on_connection_lost=true
            retry_count=10
            rate_limit_count=20
            connection_timeout=3000
            read_timeout=3000
            immediate_download=false
            add_same_download=true
            less_cpu_intensive=false
            last_saved_dir=null
            user_agent_enabled=false
            user_agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36
            """
        );
        writer.flush();
        writer.close();

        IOUtils.readConfig();
        Assertions.assertEquals("light", theme);
        Assertions.assertFalse(startup);
        Assertions.assertFalse(serverEnabled);
        Assertions.assertEquals(9563, serverPort);
        Assertions.assertTrue(triggerTurnOffOnEmptyQueue);
        Assertions.assertTrue(showCompleteDialog);
        Assertions.assertTrue(continueOnLostConnectionLost);
        Assertions.assertEquals(10, downloadRetryCount);
        Assertions.assertEquals(20, downloadRateLimitCount);
        Assertions.assertEquals(3000, connectionTimeout);
        Assertions.assertEquals(3000, readTimeout);
        Assertions.assertFalse(downloadImmediately);
        Assertions.assertTrue(addSameDownload);
        Assertions.assertFalse(lessCpuIntensive);
        Assertions.assertEquals("null", lastSavedDir);
        Assertions.assertFalse(userAgentEnabled);
        Assertions.assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36", userAgent);
    }
}