package io.beanvortex.bitkip.utils;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ValidationTests {
    @BeforeAll
    public static void initJfxRuntime() {
        // JFXPanel initializes the JavaFX environment implicitly
        new JFXPanel();
    }

    @Test
    public void testValidateChunksInput() {

        TextField textField = Mockito.spy(new TextField());

        Validations.validateChunksInput(null);
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
