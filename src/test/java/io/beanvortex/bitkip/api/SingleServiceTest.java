package io.beanvortex.bitkip.api;

import io.beanvortex.bitkip.models.SingleURLModel;
import io.beanvortex.bitkip.utils.DownloadOpUtils;
import io.beanvortex.bitkip.utils.FxUtils;
import io.beanvortex.bitkip.utils.IOUtils;
import io.helidon.common.reactive.Single;
import io.helidon.media.common.MessageBodyReadableContent;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import javafx.application.Platform;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.beanvortex.bitkip.config.AppConfigs.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SingleServiceTest {
    @Mock ServerRequest req;
    @Mock ServerResponse res;
    @Mock SingleURLModel urlModel;

    private SingleService service;

    private MockedStatic<Platform> platformMock;
    private MockedStatic<IOUtils> ioUtilsMock;
    private MockedStatic<DownloadOpUtils> downloadMock;
    private MockedStatic<FxUtils> fxUtilsMock;

    @BeforeEach
    void setUp() {
        req = mock(ServerRequest.class);
        res = mock(ServerResponse.class);
        urlModel = mock(SingleURLModel.class);
        service = new SingleService();

        // static-mock Platform
        platformMock = mockStatic(Platform.class);
        platformMock.when(() -> Platform.runLater(any()))
                .thenAnswer(inv -> { ((Runnable)inv.getArgument(0)).run(); return null; });

        fxUtilsMock = mockStatic(FxUtils.class);
        fxUtilsMock.when(() -> FxUtils.newDownloadStage(anyBoolean(), any())).thenAnswer(inv -> null);

    }

    @AfterEach
    void tearDown() {
        platformMock.close();
        if (ioUtilsMock != null) ioUtilsMock.close();
        if (downloadMock != null) downloadMock.close();
        if (fxUtilsMock != null) fxUtilsMock.close();
    }

    @Test
    void doPost_updatesAgentAndSavesConfigs() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // prepare content
        var content = mock(MessageBodyReadableContent.class);
        when(req.content()).thenReturn(content);
        when(content.as(SingleURLModel.class)).thenReturn(Single.just(urlModel));
        //when(urlModel).thenReturn(urlModel);

        // stub URL model agent
        when(urlModel.agent()).thenReturn("newAgent");
        userAgent = "origAgent";

        // static IOUtils
        ioUtilsMock = mockStatic(IOUtils.class);
        //ioUtilsMock.doNothing().when(IOUtils::saveConfigs);

        Method method = SingleService.class.getDeclaredMethod("doPost", ServerRequest.class, ServerResponse.class);
        method.setAccessible(true);
        method.invoke(service, req, res);

        // verify agent updated and configs saved
        assertEquals("newAgent", userAgent);
        ioUtilsMock.verify(IOUtils::saveConfigs);
        verify(res).status(200);
        verify(res, times(0)).send();
    }

    @Test
    void doPost_downloadImmediately_true_invokesImmediateDownload() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        downloadImmediately = true;

        var content = mock(MessageBodyReadableContent.class);
        when(req.content()).thenReturn(content);
        when(content.as(SingleURLModel.class)).thenReturn(Single.just(urlModel));
        //when(urlModel).thenReturn(any());

        downloadMock = mockStatic(DownloadOpUtils.class);
        //downloadMock.doNothing().when(() -> DownloadOpUtils.downloadImmediately(urlModel));

        Method method = SingleService.class.getDeclaredMethod("doPost", ServerRequest.class, ServerResponse.class);
        method.setAccessible(true);
        method.invoke(service, req, res);

        downloadMock.verify(() -> DownloadOpUtils.downloadImmediately(urlModel));
        verify(res).status(200);
        verify(res, times(0)).send();
    }

    @Test
    void doPost_downloadImmediately_false_opensUi() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        downloadImmediately = false;

        var content = mock(MessageBodyReadableContent.class);
        when(req.content()).thenReturn(content);
        when(content.as(SingleURLModel.class)).thenReturn(Single.just(urlModel));
        //when(urlModel).thenReturn(any());

        //fxUtilsMock.doNothing().when(() -> FxUtils.newDownloadStage(true, urlModel));

        Method method = SingleService.class.getDeclaredMethod("doPost", ServerRequest.class, ServerResponse.class);
        method.setAccessible(true);
        method.invoke(service, req, res);

        fxUtilsMock.verify(() -> FxUtils.newDownloadStage(true, urlModel));
        verify(res).status(200);
        verify(res, times(0)).send();
    }
}