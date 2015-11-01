package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Driver class of the JavaFX
 * @author Thai Kha Le
 *
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {			
			 FXMLLoader loader = new FXMLLoader();
	         loader.setLocation(LandingController.class.getResource("Landing.fxml"));
	         AnchorPane panel = (AnchorPane) loader.load();
	         Scene scene = new Scene(panel); 
	         final Stage stage = new Stage();
	         stage.setScene(scene);
	         LandingController control = loader.getController();
	         control.setMainApp(this);
	         stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
