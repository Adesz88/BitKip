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
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;

@ExtendWith(ApplicationExtension.class)
public class BatchDownloadControllerTest extends ApplicationTest
{
	Scene scene;
	BatchDownload batchDownload;
	
	@Start
	public void start(Stage stage) throws Exception
	{
		AppConfigs.initLogger();
		FXMLLoader fxmlLoader = new FXMLLoader(BitKip.class.getResource("/io/beanvortex/bitkip/fxml/batchDownload" + ".fxml"));
		Parent root = fxmlLoader.load();
		batchDownload = fxmlLoader.getController();
		
		scene = new Scene(root, 640, 480);
		
		System.out.println(scene);
		
		stage.setScene(scene);
		batchDownload.setStage(stage);
		stage.show();
		stage.toFront();
		
	}
	
	@Test
	public void testInit() {
		clickOn("#startField");
	}
	
	@Test
	public void testBatchDownload()
	{
		clickOn("#urlField").type(KeyCode.K).eraseText(1);
		clickOn("#startField").type(KeyCode.DIGIT1);
		clickOn("#endField").type(KeyCode.DIGIT2);
		clickOn("#queueCombo");
		clickOn("#chunksField").eraseText(2).type(KeyCode.DIGIT8);
		clickOn("#authenticatedCheck");
		clickOn("#usernameField").type(KeyCode.K);
		clickOn("#passwordField").type(KeyCode.P);
		clickOn("#refreshBtn");
	}
	
	@Test
	public void testDownload()
	{
		FxAssert.verifyThat("#checkBtn", NodeMatchers.isDisabled());
		String s = "https://www.url.com/file$$.zip";
		clickOn("#urlField").write(s);
		clickOn("#startField").type(KeyCode.DIGIT1);
		clickOn("#endField").type(KeyCode.DIGIT2);
		clickOn("#startField");
		FxAssert.verifyThat("#checkBtn", NodeMatchers.isEnabled());
		clickOn("#checkBtn");
	}
	
	@BeforeAll
	public static void setUp(){
		AppConfigs.initPaths();
	}
}
