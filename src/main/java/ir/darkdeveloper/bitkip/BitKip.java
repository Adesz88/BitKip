package ir.darkdeveloper.bitkip;

import io.helidon.media.jackson.JacksonSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import ir.darkdeveloper.bitkip.config.AppConfigs;
import ir.darkdeveloper.bitkip.config.observers.QueueSubject;
import ir.darkdeveloper.bitkip.repo.DownloadsRepo;
import ir.darkdeveloper.bitkip.repo.QueuesRepo;
import ir.darkdeveloper.bitkip.repo.ScheduleRepo;
import ir.darkdeveloper.bitkip.servlets.BatchService;
import ir.darkdeveloper.bitkip.servlets.SingleService;
import ir.darkdeveloper.bitkip.task.ScheduleTask;
import ir.darkdeveloper.bitkip.utils.FxUtils;
import ir.darkdeveloper.bitkip.utils.IOUtils;
import ir.darkdeveloper.bitkip.utils.MoreUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import static ir.darkdeveloper.bitkip.config.AppConfigs.*;

public class BitKip extends Application {

    private static WebServer server;

    @Override
    public void start(Stage stage) {
        IOUtils.readConfig();
        AppConfigs.initPaths();
        IOUtils.createSaveLocations();
        DownloadsRepo.createTable();
        ScheduleRepo.createSchedulesTable();
        QueuesRepo.createTable();
        var queues = QueuesRepo.getAllQueues(false, true);
        if (queues.isEmpty())
            queues = QueuesRepo.createDefaultRecords();
        queues = ScheduleRepo.createDefaultSchedulesForQueues(queues);
        QueueSubject.addAllQueues(queues);
        IOUtils.createFoldersForQueue();
        hostServices = getHostServices();
        FxUtils.startMainStage(stage);
        ScheduleTask.scheduleQueues();
        MoreUtils.checkUpdates(false);
        IOUtils.moveChunkFilesToTemp(downloadPath);
        initTray(stage);
        startServer();
    }


    private void initTray(Stage stage) {
        if (SystemTray.isSupported()) {
            Platform.setImplicitExit(false);
            var tray = SystemTray.getSystemTray();
            var image = Toolkit.getDefaultToolkit().getImage(getResource("icons/logo.png"));
            var popup = new PopupMenu();
            var openItem = new MenuItem("Open App");
            var exitItem = new MenuItem("Exit App");
            ActionListener openListener = e -> Platform.runLater(() -> {
                if (stage.isShowing())
                    stage.toFront();
                else stage.show();
            });
            openItem.addActionListener(openListener);
            exitItem.addActionListener(e -> stop());
            popup.add(openItem);
            popup.add(exitItem);
            var trayIcon = new TrayIcon(image, "BitKip", popup);
            trayIcon.addActionListener(openListener);
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                log.error(e.getLocalizedMessage());
            }

        }

    }


    @Override
    public void stop() {
        var notObservedDms = new ArrayList<>(currentDownloadings);
        notObservedDms.forEach(dm -> dm.getDownloadTask().pause());
        startedQueues.clear();
        currentSchedules.values().forEach(sm -> {
            var startScheduler = sm.getStartScheduler();
            var stopScheduler = sm.getStopScheduler();
            if (startScheduler != null)
                startScheduler.shutdownNow();
            if (stopScheduler != null)
                stopScheduler.shutdownNow();
        });
        try {
            if (server != null)
                server.shutdown();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        System.exit(0);
    }


    public static void main(String[] args) {
        initLogger();
        launch();
    }

    private static void startServer() {
        if (serverEnabled) {

            var routing = Routing.builder()
                    .register("/single", new SingleService())
                    .register("/batch", new BatchService())
                    .build();
            var jacksonSupport = JacksonSupport.create();
            server = WebServer.builder()
                    .bindAddress("127.0.0.1")
                    .port(serverPort)
                    .addRouting(routing)
                    .addMediaSupport(jacksonSupport)
                    .build();
            server.start().exceptionally(throwable -> {
                var header = "Failed to start server. Is there another instance running?\nIf not you may need to change application server port and restart";
                log.error(header);
                Platform.runLater(() -> {
                    if (FxUtils.showFailedToStart(header, throwable.getCause().getLocalizedMessage()))
                        Platform.exit();
                });
                return null;
            });
        }

    }

    public static URL getResource(String path) {
        return BitKip.class.getResource(path);
    }
}