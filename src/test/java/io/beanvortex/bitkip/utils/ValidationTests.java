package io.beanvortex.bitkip.utils;

import io.beanvortex.bitkip.config.AppConfigs;
import io.beanvortex.bitkip.models.DownloadModel;
import io.beanvortex.bitkip.models.DownloadStatus;
import io.beanvortex.bitkip.repo.QueuesRepo;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.beanvortex.bitkip.config.AppConfigs.downloadPath;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ValidationTests {
    @BeforeAll
    public static void initJfxRuntime() {
        // JFXPanel initializes the JavaFX environment implicitly
        new JFXPanel();
    }

    @Test
    public void testValidateUri(){
        Assertions.assertTrue(Validations.validateUri("http://www.google.com"));
        Assertions.assertTrue(Validations.validateUri("https://www.google.com"));
        Assertions.assertTrue(Validations.validateUri("ftp://www.google.com"));
        Assertions.assertFalse(Validations.validateUri("test://www.google.com"));
    }

    @Test
    public void testFixUriChars(){
        Assertions.assertEquals(Validations.fixURIChars("{}[]`\" <>"), "%7b%7d%5b%5d%60%22%20%3c%3e");
    }

    @Test
    public void testMaxChunks(){
        int cpu = Runtime.getRuntime().availableProcessors();
        Assertions.assertEquals(0, Validations.maxChunks(1_999_999));
        Assertions.assertEquals(cpu < 10 ? cpu*2 : cpu , Validations.maxChunks(2_000_000));
    }

    @Test
    @SneakyThrows
    public void testFillNotFetchedData() {
        DownloadModel dm = DownloadModel.builder()
                .name("teszt").progress(0).downloaded(0).size(1254631)
                .uri(UUID.randomUUID().toString()).filePath(downloadPath + "test")
                .chunks(8).addDate(LocalDateTime.now()).addToQueueDate(LocalDateTime.now())
                .lastTryDate(LocalDateTime.now()).completeDate(LocalDateTime.now())
                .queues(new CopyOnWriteArrayList<>(List.of(QueuesRepo.findByName(Defaults.ALL_DOWNLOADS_QUEUE, false))))
                .openAfterComplete(false).showCompleteDialog(false).downloadStatus(DownloadStatus.Paused)
                .resumable(true)
                .build();
        DownloadModel original = dm;
        Validations.fillNotFetchedData(dm);
        Assertions.assertEquals(original, dm);
    }

    @Test
    public void testQueueDoesNotExist() {
        AppConfigs.initLogger();
        
        
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> DownloadModel.builder()
                .name("teszt").progress(0).downloaded(0).size(1254631)
                .uri(UUID.randomUUID().toString()).filePath(downloadPath + "test")
                .chunks(8).addDate(LocalDateTime.now()).addToQueueDate(LocalDateTime.now())
                .lastTryDate(LocalDateTime.now()).completeDate(LocalDateTime.now())
                .queues(new CopyOnWriteArrayList<>(List.of(QueuesRepo.findByName("gasgvas", false))))
                .openAfterComplete(false).showCompleteDialog(false).downloadStatus(DownloadStatus.Paused)
                .resumable(true)
                .build());
        String errorMessage = exception.getMessage();
        String expectedMessage = "Queue does not exist";
        
        Assertions.assertTrue(expectedMessage.equals(errorMessage));
    }

    @Test
    public void testValidateChunksInput() {

        TextField textField = Mockito.spy(new TextField());

        Validations.validateChunksInput(null); // check this case
        Mockito.verifyNoInteractions(textField);

        Validations.validateChunksInput(textField);
        Mockito.verify(textField).setText(String.valueOf(Validations.maxChunks(Long.MAX_VALUE)));
    }

    @Test
    void testTextInputListener() {
        TextField field = new TextField();
        Validations.validateSpeedInput(field);

        field.setText("abc123!@#");
        // Trigger listener manually
        field.textProperty().set(field.getText());

        Assertions.assertEquals("123", field.getText());
    }

    @Test
    void testValidateIntInputCheckWithEmptyValue() {
        TextField field = new TextField();
        Validations.validateIntInputCheck(field, 0L, 0, 100);

        field.setText("1");
        field.textProperty().set(field.getText());
        field.setText("");
        field.textProperty().set(field.getText());

        Assertions.assertEquals("0", field.getText());
    }

    @Test
    void testValidateIntInputCheck() {
        TextField field = new TextField();
        Validations.validateIntInputCheck(field, 0L, 0, 100);

        field.setText("abc12!@#");
        field.textProperty().set(field.getText());

        Assertions.assertEquals("12", field.getText());
    }

    @Test
    void testValidateIntInputCheckWithZerosPrefix() {
        TextField field = new TextField();
        Validations.validateIntInputCheck(field, 0L, 0, 100);

        field.setText("00002");
        field.textProperty().set(field.getText());

        Assertions.assertEquals("00002", field.getText());
    }

    @Test
    void testValidateIntInputCheckWithZeros() {
        TextField field = new TextField();
        Validations.validateIntInputCheck(field, 0L, -5, 100);

        field.setText("0000");
        field.textProperty().set(field.getText());

        Assertions.assertEquals("5", field.getText());
    }

    @Test
    void testValidateIntInputCheckWithMinAndMax() {
        TextField field = new TextField();
        Validations.validateIntInputCheck(field, 0L, 5, 10);

        field.setText("2");
        field.textProperty().set(field.getText());

        Assertions.assertEquals("5", field.getText());

        field.setText("15");
        field.textProperty().set(field.getText());

        Assertions.assertEquals("10", field.getText());
    }

    @Test
    void testValidateIntInputCheckWithoutMinAndMax() {
        TextField field = new TextField();
        Validations.validateIntInputCheck(field, 0L, null, null);

        field.setText("2");
        field.textProperty().set(field.getText());

        Assertions.assertEquals("2", field.getText());

        field.setText("1253000000");
        field.textProperty().set(field.getText());

        Assertions.assertEquals("1253000000", field.getText());
    }
}
