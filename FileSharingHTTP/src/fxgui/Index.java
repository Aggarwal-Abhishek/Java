package fxgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Index extends Application{

	@Override
	public void start(Stage window) throws Exception {
		
		window.setTitle("My Server");
		window.setResizable(false);
		
		window.setScene(new Scene(FXMLLoader.load(getClass().getResource("Index.fxml"))));
		window.show();
	}

}
