package io.beanvortex.bitkip.controllers;

import io.beanvortex.bitkip.BitKip;
import io.beanvortex.bitkip.config.AppConfigs;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

@ExtendWith(ApplicationExtension.class)
public class LogsControllerTest extends ApplicationTest
{
	LogsController controller;
	Scene scene;
	
	@Start
	public void start(Stage stage) throws IOException
	{
		
		AppConfigs.initLogger();
		FXMLLoader fxmlLoader = new FXMLLoader(BitKip.class.getResource("/io/beanvortex/bitkip/fxml/logs.fxml"));
		Parent root = fxmlLoader.load();
		controller = fxmlLoader.getController();
		
		scene = new Scene(root, 1280, 960);
		
		System.out.println(scene);
		
		stage.setScene(scene);
		controller.setStage(stage);
		stage.show();
		stage.toFront();
	}
	
	@Test
	public void testBasics() throws InterruptedException
	{
		ComboBox<?> combo = lookup("#comboSelectFile").query();
		for (Node child : combo.getChildrenUnmodifiable()) {
			if (child.getStyleClass().contains("arrow-button")) {
				Node arrowRegion = ((Pane) child).getChildren().get(0);
				clickOn(arrowRegion);
				Thread.sleep(1000);
				moveTo(arrowRegion).moveBy(-20,20).clickOn();
			}
		}
		clickOn("#text");
	}
}
