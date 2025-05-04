package io.beanvortex.bitkip.controllers;

import io.beanvortex.bitkip.BitKip;
import io.beanvortex.bitkip.utils.FxUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
@Disabled
public class AboutControllerTest extends ApplicationTest {

    Scene scene;

    @Start
    public void start(Stage stage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(BitKip.class.getResource("about" + ".fxml"));
        Object root = fxmlLoader.load();
        Parent parent = (Parent) root;

        scene = new Scene(parent, 640, 480);

        System.out.println(scene);

        stage.setScene(scene);
        stage.show();
        stage.toFront();

    }

    @Test
    public void test() {
        System.out.println(scene);
    }
}
