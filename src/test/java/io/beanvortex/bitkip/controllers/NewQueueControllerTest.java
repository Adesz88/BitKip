package io.beanvortex.bitkip.controllers;

import io.beanvortex.bitkip.BitKip;
import io.beanvortex.bitkip.config.AppConfigs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;

public class NewQueueControllerTest extends ApplicationTest {
    Scene scene;
    NewQueueController newQueueController;

    @Start
    public void start(Stage stage) throws Exception
    {
        AppConfigs.initLogger();
        FXMLLoader fxmlLoader = new FXMLLoader(BitKip.class.getResource("/io/beanvortex/bitkip/fxml/newQueue" + ".fxml"));
        Parent root = fxmlLoader.load();
        newQueueController = fxmlLoader.getController();

        scene = new Scene(root, 640, 480);

        System.out.println(scene);

        stage.setScene(scene);
        newQueueController.setStage(stage);
        stage.show();
        stage.toFront();

    }

    @Test
    public void testCreate() {
        clickOn("#queueField").write("test1");
        clickOn("Add");
    }

    @BeforeAll
    public static void setUp(){
        AppConfigs.initPaths();
    }
}
