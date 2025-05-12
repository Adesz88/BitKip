package io.beanvortex.bitkip.controllers;

import io.beanvortex.bitkip.BitKip;
import io.beanvortex.bitkip.config.AppConfigs;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

@ExtendWith(ApplicationExtension.class)
public class MainControllerTest extends ApplicationTest
{
	MainController main;
	Scene scene;
	
	@Start
	public void start(Stage stage) throws Exception
	{
		AppConfigs.initLogger();
		FXMLLoader fxmlLoader = new FXMLLoader(BitKip.class.getResource("/io/beanvortex/bitkip/fxml/main" + ".fxml"));
		Parent root = fxmlLoader.load();
		main = fxmlLoader.getController();
		
		scene = new Scene(root, 640, 480);
		
		System.out.println(scene);
		
		stage.setScene(scene);
		main.setStage(stage);
		stage.show();
		stage.toFront();
	}
	
	@Test
	public void testSideBar(){
		clickOn("#sideTree");
		List<String> elements = List.of("All", "Categories", "All Downloads", "Compressed",
			"Music", "Videos", "Programs", "Docs", "Others", "Finished", "Unfinished", "Queues");
		for(String element : elements){
			clickOn((Node)lookup(LabeledMatchers.hasText(element)).query());
		}
	}
}
