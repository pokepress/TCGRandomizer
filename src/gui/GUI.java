package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class GUI extends Application {
	
	private Stage primaryStage;
	private AnchorPane ap;
			
	@Override
	public void start (Stage primaryStage) throws IOException {
		
		setPrimaryStage(primaryStage);
		FXMLLoader loader = new FXMLLoader();	
		loader.setLocation(getClass().getResource("GUI.fxml"));
		ap = loader.load();
		primaryStage.setTitle("TCG Randomizer");
		primaryStage.setScene(new Scene(ap));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main (String[] args) {		
		launch();
	}

	public Stage getPrimaryStage (Event e) {
		return primaryStage;
	}

	public void setPrimaryStage (Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public AnchorPane getAnchorPane() {
		return ap;
	}

	public void setAnchorPane(AnchorPane ap) {
		this.ap = ap;
	}	
}
