package io.beanvortex.bitkip.controllers;

import io.beanvortex.bitkip.BitKip;
import io.beanvortex.bitkip.config.AppConfigs;
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

@ExtendWith(ApplicationExtension.class)
public class AboutControllerTest extends ApplicationTest {

    Scene scene;
    AboutController aboutController;

    @Start
    public void start(Stage stage) throws Exception {
        AppConfigs.initLogger();
        FXMLLoader fxmlLoader = new FXMLLoader(BitKip.class.getResource("/io/beanvortex/bitkip/fxml/about" + ".fxml"));
        Parent root = fxmlLoader.load();
        aboutController = fxmlLoader.getController();

        scene = new Scene(root, 640, 480);

        System.out.println(scene);

        stage.setScene(scene);
        aboutController.setStage(stage);
        stage.show();
        stage.toFront();

    }

    @Test
    public void test() {
        clickOn("#checkForUpdateBtn");
        Assertions.assertNotNull(aboutController.getStage());
    }

    @Test
    @Disabled
    public void test2() {
        clickOn("#linkGit");
    }
}
