package io.beanvortex.bitkip.api;

import io.beanvortex.bitkip.models.BatchURLModel;
import io.beanvortex.bitkip.models.LinkModel;
import io.beanvortex.bitkip.models.QueueModel;
import io.beanvortex.bitkip.repo.QueuesRepo;
import io.beanvortex.bitkip.utils.FxUtils;
import io.beanvortex.bitkip.utils.Validations;
import io.helidon.media.common.MessageBodyReadableContent;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.common.reactive.Single;
import javafx.application.Platform;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static io.beanvortex.bitkip.utils.Defaults.ALL_DOWNLOADS_QUEUE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchServiceTest {

    @Mock ServerRequest req;
    @Mock ServerResponse res;
    @Mock BatchURLModel urlModel;
    @Mock QueueModel fakeQueue;

    private BatchService service;

    private MockedStatic<Platform> platformMock;
    private MockedStatic<Validations> validationsMock;
    private MockedStatic<QueuesRepo> queuesRepoMock;
    private MockedStatic<FxUtils> fxUtilsMock;

    @BeforeEach
    void setUp() {
        service = new BatchService();
        // static-mock Platform.runLater so Runnable runs immediately
        platformMock = mockStatic(Platform.class);
        platformMock.when(() -> Platform.runLater(any()))
                .thenAnswer(inv -> { ((Runnable)inv.getArgument(0)).run(); return null; });

        // Stub validations
        validationsMock = mockStatic(Validations.class);
        validationsMock.when(() -> Validations.maxChunks(anyLong()))
                .thenReturn(42);

        // Stub queues repo
        queuesRepoMock = mockStatic(QueuesRepo.class);
        queuesRepoMock.when(() -> QueuesRepo.findByName(ALL_DOWNLOADS_QUEUE, false))
                .thenReturn(fakeQueue);
    }

    @AfterEach
    void tearDown() {
        platformMock.close();
        validationsMock.close();
        queuesRepoMock.close();
        if (fxUtilsMock != null) fxUtilsMock.close();
    }

    @Test
    void convertToLinks_nullOrEmpty_returnsEmpty() throws Exception {
        Method m = BatchService.class.getDeclaredMethod("convertToLinks", BatchURLModel.class);
        m.setAccessible(true);

        when(urlModel.links()).thenReturn(null);
        @SuppressWarnings("unchecked")
        List<LinkModel> result1 = (List<LinkModel>) m.invoke(service, urlModel);
        assertTrue(result1.isEmpty());

        when(urlModel.links()).thenReturn(List.of());
        @SuppressWarnings("unchecked")
        List<LinkModel> result2 = (List<LinkModel>) m.invoke(service, urlModel);
        assertTrue(result2.isEmpty());
    }

    @Test
    void convertToLinks_valid_createsLinkModelsWithQueue() throws Exception {
        Method m = BatchService.class.getDeclaredMethod("convertToLinks", BatchURLModel.class);
        m.setAccessible(true);

        List<String> urls = List.of("http://a", "http://b");
        when(urlModel.links()).thenReturn(urls);

        @SuppressWarnings("unchecked")
        List<LinkModel> links = (List<LinkModel>) m.invoke(service, urlModel);

        assertEquals(2, links.size());
        assertEquals("http://a", links.get(0).getUri());
        assertEquals(42, links.get(0).getChunks());
        assertTrue(links.get(0).getQueues().contains(fakeQueue));
    }

    @Test
    void doPost_emptyLinks_sends400() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // stub reactive content
        var content = mock(MessageBodyReadableContent.class);
        when(req.content()).thenReturn(content);
        when(content.as(BatchURLModel.class)).thenReturn(Single.just(urlModel));
        when(urlModel.links()).thenReturn(List.of());

        Method method = BatchService.class.getDeclaredMethod("doPost", ServerRequest.class, ServerResponse.class);
        method.setAccessible(true);
        method.invoke(service, req, res);

        verify(res).status(400);
        //verify(res).send("Empty data sent by extension");
    }

    @Test
    void doPost_valid_sends200_andOpensUi() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var content = mock(MessageBodyReadableContent.class);
        when(req.content()).thenReturn(content);
        when(content.as(BatchURLModel.class)).thenReturn(Single.just(urlModel));
        when(urlModel.links()).thenReturn(List.of("x"));

        // Stub static UI
        fxUtilsMock = mockStatic(FxUtils.class);
        //fxUtilsMock.doNothing().when(() -> FxUtils.newBatchListStage(anyList(), isNull()));

        Method method = BatchService.class.getDeclaredMethod("doPost", ServerRequest.class, ServerResponse.class);
        method.setAccessible(true);
        method.invoke(service, req, res);

        verify(res).status(200);
        verify(res, times(0)).send();
        fxUtilsMock.verify(() -> FxUtils.newBatchListStage(anyList(), isNull()));
    }
}