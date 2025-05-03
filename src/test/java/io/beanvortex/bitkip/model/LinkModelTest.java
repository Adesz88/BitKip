package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.LinkModel;
import io.beanvortex.bitkip.models.QueueModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class LinkModelTest {
    @Test
    public void testNullLinkModel() {
        LinkModel linkModel = new LinkModel("123Test", 2);
        Assertions.assertEquals("0", linkModel.getSizeString());
        Assertions.assertEquals("", linkModel.getResumableString());
        Assertions.assertNull(linkModel.getSelectedPath());
    }

    @Test
    public void testFullLinkModel() {
        QueueModel queueModel = new QueueModel("test1", false);
        QueueModel queueModel2 = new QueueModel("test2", false);
        QueueModel queueModel3 = new QueueModel("test3", false);
        LinkModel link = new LinkModel();
        link.setUri("https://example.com/file.zip");
        link.setSize(2048); // 2 kB
        link.setChunks(4);
        link.setResumable(true);
        link.setName("file.zip");
        link.setPath("/downloads");
        link.setSelectedPath("/downloads/custom");
        link.getQueues().add(queueModel);
        link.setSizeString("1 MB");
        link.setResumableString("yes");
        link.setQueuesString("Main Queue");

        Assertions.assertEquals("2 kB", link.getSizeString());
        Assertions.assertEquals("yes", link.getResumableString());
        Assertions.assertEquals("/downloads/custom", link.getSelectedPath());
        Assertions.assertEquals("test1", link.getQueuesString());

        //Changing values
        link.setResumable(false);
        link.getQueues().add(queueModel2);
        link.getQueues().add(queueModel3);
        Assertions.assertEquals("no", link.getResumableString());
        Assertions.assertEquals("test3", link.getQueuesString());
    }
}
