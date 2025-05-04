package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.QueueModel;
import io.beanvortex.bitkip.models.StartedQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StartedQueueTest {
    @Test
    public void testEquals() {
        QueueModel queue = new QueueModel();
        queue.setId(1);
        StartedQueue startedQueue = new StartedQueue(queue);

        Assertions.assertEquals(startedQueue, startedQueue);
        Assertions.assertNotEquals(startedQueue, null);
        Assertions.assertNotEquals(startedQueue, "test");

        QueueModel queue2 = new QueueModel();
        queue2.setId(2);
        StartedQueue startedQueue2 = new StartedQueue(queue2);
        Assertions.assertNotEquals(startedQueue, startedQueue2);
    }
}
