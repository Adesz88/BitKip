package io.beanvortex.bitkip.utils;

import io.beanvortex.bitkip.config.AppConfigs;
import io.beanvortex.bitkip.controllers.BatchList;
import io.beanvortex.bitkip.models.LinkModel;
import io.beanvortex.bitkip.models.QueueModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.beanvortex.bitkip.utils.Defaults.ALL_DOWNLOADS_QUEUE;
import static io.beanvortex.bitkip.utils.Defaults.PROGRAMS_QUEUE;

public class LinkTableUtilsTest {
    @Test
    public void testChangeLinkQueuesWithAllDownloads() {
        List<LinkModel> links = new ArrayList<>();
        LinkTableUtils linkTableUtils = new LinkTableUtils(null, links, null, null, null);

        LinkModel linkModel = new LinkModel();

        QueueModel queueModel = new QueueModel();
        queueModel.setName(ALL_DOWNLOADS_QUEUE);
        queueModel.setHasFolder(false);

        linkTableUtils.changeLinkQueues(linkModel, queueModel, null, false);
        Assertions.assertNull(linkModel.getPath());
    }

    @Test
    public void testChangeLinkQueuesWithoutFolder() {
        String fileName = "bitKip.jar";
        AppConfigs.initPaths();

        List<LinkModel> links = new ArrayList<>();
        LinkTableUtils linkTableUtils = new LinkTableUtils(null, links, null, null, null);

        LinkModel linkModel = new LinkModel();

        QueueModel queueModel = new QueueModel();
        queueModel.setName(PROGRAMS_QUEUE);
        queueModel.setHasFolder(false);
        linkModel.setName(fileName);

        BatchList.LocationData location = new BatchList.LocationData(null, false);

        linkTableUtils.changeLinkQueues(linkModel, queueModel, location, false);
        Assertions.assertTrue(linkModel.getQueues().contains(queueModel));
        String expectedLocation = DownloadUtils.determineLocation(fileName);
        Assertions.assertEquals(expectedLocation, linkModel.getPath());
    }

    @Test
    public void testChangeLinkQueuesWithFolder() {
        String fileName = "bitKip.jar";
        AppConfigs.initPaths();

        List<LinkModel> links = new ArrayList<>();
        LinkTableUtils linkTableUtils = new LinkTableUtils(null, links, null, null, null);

        LinkModel linkModel = new LinkModel();

        QueueModel queueModel = new QueueModel();
        queueModel.setName(PROGRAMS_QUEUE);
        queueModel.setHasFolder(true);
        linkModel.setName(fileName);

        linkTableUtils.changeLinkQueues(linkModel, queueModel, null, false);
        Assertions.assertTrue(linkModel.getQueues().contains(queueModel));
        String expectedPostfix = "Queues" + File.separator + "Programs" + File.separator;
        Assertions.assertTrue(linkModel.getPath().endsWith(expectedPostfix));
    }
}
