package io.beanvortex.bitkip.controllers;

import io.beanvortex.bitkip.config.AppConfigs;
import io.beanvortex.bitkip.models.Credentials;
import io.beanvortex.bitkip.utils.FxUtils;
import io.beanvortex.bitkip.config.observers.QueueObserver;
import io.beanvortex.bitkip.config.observers.QueueSubject;
import io.beanvortex.bitkip.models.LinkModel;
import io.beanvortex.bitkip.models.QueueModel;
import io.beanvortex.bitkip.repo.DownloadsRepo;
import io.beanvortex.bitkip.repo.QueuesRepo;
import io.beanvortex.bitkip.utils.Validations;
import io.beanvortex.bitkip.utils.DownloadUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static io.beanvortex.bitkip.config.AppConfigs.*;
import static io.beanvortex.bitkip.config.observers.QueueSubject.getQueueSubject;
import static io.beanvortex.bitkip.utils.Defaults.ALL_DOWNLOADS_QUEUE;
import static io.beanvortex.bitkip.utils.Defaults.extensions;
import static io.beanvortex.bitkip.utils.DownloadUtils.handleError;

public class BatchDownload implements QueueObserver {

    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button questionBtnUri, refreshBtn, checkBtn, cancelBtn, questionBtnChunks, openLocation, newQueue;
    @FXML
    private ComboBox<QueueModel> queueCombo;
    @FXML
    private TextField chunksField, urlField, usernameField, startField, locationField, endField;
    @FXML
    private CheckBox lastLocationCheck, authenticatedCheck;

    private LinkModel tempLink;
    private Stage stage;


    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        initAfterStage();
    }

    @Override
    public void initAfterStage() {
        updateTheme(stage.getScene());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkBtn.setGraphic(new FontIcon());
        cancelBtn.setGraphic(new FontIcon());
        questionBtnChunks.setGraphic(new FontIcon());
        openLocation.setGraphic(new FontIcon());
        questionBtnUri.setGraphic(new FontIcon());
        newQueue.setGraphic(new FontIcon());
        var queues = QueueSubject.getQueues();
        if (queues.isEmpty())
            queues = QueuesRepo.getAllQueues(false, false);
        queues = queues.stream().filter(QueueModel::isCanAddDownload).toList();
        queueCombo.getItems().addAll(queues);
        queueCombo.setValue(queues.get(0));
        errorLabel.setVisible(false);
        checkBtn.setDisable(true);
        usernameField.getParent().setManaged(false);
        usernameField.getParent().setVisible(false);
        passwordField.getParent().setManaged(false);
        passwordField.getParent().setVisible(false);
        refreshBtn.setGraphic(new FontIcon());
        refreshBtn.setOnAction(e -> autoFillLocation());
        if (AppConfigs.lastSavedDir == null)
            lastLocationCheck.setDisable(true);
        Validations.prepareLinkFromClipboard(urlField);
        Validations.validateChunksInput(chunksField);
        Validations.validateIntInputCheck(startField, 0L, 0, null);
        Validations.validateIntInputCheck(endField, 0L, 0, null);
        var questionBtns = new Button[]{questionBtnUri, questionBtnChunks};
        var contents = new String[]{
                "You want to download several files, clarify where urls are different by $ sign." +
                        " (for example 'https://www.url.com/file00.zip', change 00 to $$)",
                "Every single file is seperated into parts and will be downloaded concurrently"
        };
        DownloadUtils.initPopOvers(questionBtns, contents);
        autoFillLocation();
        queueCombo.getSelectionModel().selectedIndexProperty().addListener(observable -> onQueueChanged());
        startField.textProperty().addListener(o -> autoFillLocation());
        endField.textProperty().addListener(o -> autoFillLocation());
        urlField.textProperty().addListener((o, ol, n) -> {
            if (n.isBlank())
                DownloadUtils.disableControlsAndShowError("URL is blank", errorLabel, checkBtn, null);
            else autoFillLocation();
        });
        locationField.textProperty().addListener((o, ol, n) -> {
            if (n.isBlank())
                DownloadUtils.disableControlsAndShowError("Location is blank", errorLabel, checkBtn, null);
            else onOfflineFieldsChanged();
        });
    }

    private void onOfflineFieldsChanged() {
        if (tempLink != null)
            handleError(() -> DownloadUtils.onOfflineFieldsChanged(locationField, tempLink.getName(),
                    null, queueCombo, null, checkBtn, openLocation, lastLocationCheck), errorLabel);

    }

    private void autoFillLocation() {
        try {
            queueCombo.getSelectionModel().select(queueCombo.getSelectionModel().getSelectedItem());
            var url = urlField.getText();
            var start = Integer.parseInt(startField.getText());
            var end = Integer.parseInt(endField.getText());
            var links = generateLinks(url, start, end, Integer.parseInt(chunksField.getText()), true);
            var link = links.get(0);
            tempLink = link;
            var credential = new Credentials(usernameField.getText(), passwordField.getText());
            var connection = DownloadUtils.connect(link.getUri(), credential);
            var fileNameLocationFuture = CompletableFuture.supplyAsync(() -> DownloadUtils.extractFileName(link.getUri(), connection))
                    .thenAccept(this::setLocation);
            fileNameLocationFuture
                    .whenComplete((unused, throwable) ->
                            handleError(() -> DownloadUtils.checkIfFileIsOKToSave(locationField.getText(),
                                    tempLink.getName(), null, checkBtn, lastLocationCheck), errorLabel))
                    .exceptionally(throwable -> {
                        var errorMsg = throwable.toString();
                        Platform.runLater(() ->
                                DownloadUtils.disableControlsAndShowError(errorMsg, errorLabel,
                                        checkBtn, null));
                        return null;
                    });
        } catch (NumberFormatException ignore) {
        } catch (Exception e) {
            var errorMsg = e.toString();
            if (e instanceof IndexOutOfBoundsException)
                errorMsg = "No URLs found";
            DownloadUtils.disableControlsAndShowError(errorMsg, errorLabel, checkBtn, null);
        }
    }

    private void setLocation(String fileName) {
        DownloadUtils.setLocationAndQueue(locationField, fileName, null);
        tempLink.setName(fileName);
    }

    public List<LinkModel> generateLinks(String url, int start, int end, int chunks, boolean oneLink) {
        if (start > end)
            throw new IllegalArgumentException("Start value cannot be greater than end value");

        var signsIndex = new ArrayList<Integer>();
        for (int i = 0; i < url.length(); i++)
            if (url.charAt(i) == '$')
                signsIndex.add(i);

        if (signsIndex.isEmpty())
            throw new IllegalArgumentException("No pattern found in url");

        if (signsIndex.size() < digits(start) || signsIndex.size() < digits(end))
            throw new IllegalArgumentException("Not enough pattern to cover the range");


        var links = new ArrayList<LinkModel>();
        for (int i = start; i < end + 1; i++) {
            var x = i / 10;
            if (x == 0) {
                String link = url;
                for (int j = 0; j < signsIndex.size(); j++) {
                    if (j == signsIndex.size() - 1)
                        link = replaceDollarOnce(link, (char) (i + 48));
                    else
                        link = replaceDollarOnce(link, '0');
                }
                links.add(new LinkModel(link, chunks));
            } else {
                StringBuilder link = new StringBuilder(url);
                var digitsToFill = signsIndex.size();
                var iDigits = digits(i);
                var diff = digitsToFill - iDigits;
                for (int j = 0; j < diff; j++)
                    link.setCharAt(signsIndex.get(j), '0');

                var cpI = i;
                for (int j = 0; j < signsIndex.size() - diff; j++) {
                    link.setCharAt(link.lastIndexOf("$"), (char) (cpI % 10 + 48));
                    cpI /= 10;
                }
                links.add(new LinkModel(link.toString(), chunks));
            }
            if (oneLink)
                break;
        }

        return links;
    }

    private String replaceDollarOnce(String str, char replace) {
        var chars = str.toCharArray();
        for (int i = 0; i < str.length(); i++)
            if ('$' == chars[i]) {
                chars[i] = replace;
                break;
            }
        return String.copyValueOf(chars);
    }

    private int digits(int num) {
        var count = 0;
        while (num != 0) {
            count++;
            num /= 10;
        }
        return count;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @FXML
    private void onSelectLocation(ActionEvent e) {
        var path = DownloadUtils.selectLocation(FxUtils.getStageFromEvent(e));
        if (path != null)
            locationField.setText(path);
        if (tempLink != null)
            handleError(() -> DownloadUtils.checkIfFileIsOKToSave(locationField.getText(),
                    tempLink.getName(), null, checkBtn, lastLocationCheck), errorLabel);
    }

    @FXML
    private void onCheck() {
        try {
            var url = urlField.getText();
            var path = locationField.getText();
            if (lastLocationCheck.isSelected())
                path = AppConfigs.lastSavedDir;
            var finalPath = path;
            if (url.isBlank()) {
                log.warn("URL is blank");
                DownloadUtils.disableControlsAndShowError("URL is blank", errorLabel, checkBtn, null);
                return;
            }
            if (path.isBlank()) {
                log.warn("Location is blank");
                DownloadUtils.disableControlsAndShowError("Location is blank", errorLabel, checkBtn, null);
                return;
            }
            var start = Integer.parseInt(startField.getText());
            var end = Integer.parseInt(endField.getText());
            var links = generateLinks(url, start, end, Integer.parseInt(chunksField.getText()), false);

            if (!addSameDownload)
                for (var link : links) {
                    var byURL = DownloadsRepo.findByURL(link.getUri());
                    if (!byURL.isEmpty()) {
                        var found = byURL.stream()
                                .filter(dm -> {
                                    var s = Paths.get(dm.getFilePath()).getParent().toString() + File.separator;
                                    return s.equals(finalPath);
                                })
                                .findFirst();
                        if (found.isPresent()) {
                            var msg = "At least one URL exists for this location. Change location or change start, end.\n"
                                    + found.get().getUri();
                            log.warn(msg);
                            DownloadUtils.disableControlsAndShowError(msg, errorLabel, checkBtn, null);
                            return;
                        }
                    }
                }
            var selectedQueue = queueCombo.getSelectionModel().getSelectedItem();
            var allDownloadsQueue = QueuesRepo.findByName(ALL_DOWNLOADS_QUEUE, false);
            var secondaryQueue = getSecondaryQueueByFileName(tempLink.getName());
            links.forEach(lm -> {
                lm.getQueues().add(allDownloadsQueue);
                lm.getQueues().add(secondaryQueue);
                if (selectedQueue.getId() != allDownloadsQueue.getId())
                    lm.getQueues().add(selectedQueue);
                lm.setPath(finalPath);
                lm.setSelectedPath(finalPath);
            });
            Credentials credentials = null;
            if (!usernameField.getText().isBlank() && !passwordField.getText().isBlank())
                credentials = new Credentials(usernameField.getText(), passwordField.getText());
            FxUtils.newBatchListStage(links, credentials);
            getQueueSubject().removeObserver(this);
            stage.close();
        } catch (IllegalArgumentException e) {
            if (e instanceof NumberFormatException)
                return;
            log.error(e.toString());
            DownloadUtils.disableControlsAndShowError(e.toString(), errorLabel, checkBtn, null);
        }
    }

    public static QueueModel getSecondaryQueueByFileName(String fileName) {
        if (fileName == null || fileName.isBlank())
            return null;
        for (var entry : extensions.entrySet()) {
            // empty is set for others
            if (entry.getValue().isEmpty())
                return QueuesRepo.findByName(entry.getKey(), false);

            var matched = entry.getValue().stream().anyMatch(fileName::endsWith);
            if (matched)
                return QueuesRepo.findByName(entry.getKey(), false);
        }
        return null;
    }


    @FXML
    private void onNewQueue() {
        FxUtils.newQueueStage();
    }

    @FXML
    private void onCancel() {
        getQueueSubject().removeObserver(this);
        stage.close();
    }

    @Override
    public void updateQueue() {
        updateQueueData(queueCombo);
    }

    public static void updateQueueData(ComboBox<QueueModel> queueCombo) {
        var queues = QueueSubject.getQueues();
        if (queues.isEmpty())
            queues = QueuesRepo.getAllQueues(false, false);
        queues = queues.stream().filter(QueueModel::isCanAddDownload).toList();
        queueCombo.getItems().clear();
        queueCombo.getItems().addAll(queues);
        queueCombo.setValue(queues.get(0));
    }

    @FXML
    private void onQueueChanged() {
        onOfflineFieldsChanged();
    }

    @FXML
    private void onLastLocationCheck() {
        if (lastLocationCheck.isSelected())
            locationField.setText(AppConfigs.lastSavedDir);
        else
            setLocation(tempLink.getName());
    }

    @FXML
    private void onAuthenticatedCheck() {
        usernameField.getParent().setManaged(authenticatedCheck.isSelected());
        usernameField.getParent().setVisible(authenticatedCheck.isSelected());
        passwordField.getParent().setManaged(authenticatedCheck.isSelected());
        passwordField.getParent().setVisible(authenticatedCheck.isSelected());
        usernameField.setText("");
        passwordField.setText("");
    }
}


