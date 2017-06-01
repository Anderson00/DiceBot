package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sites.client999dice.DiceWebAPI;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


public class Main extends Application {	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println((999999.0) * (49.95 / 100.0));
			ApplicationSingleton.getInstance().setApplication(this);// init singleton			
			StackPane root = FXMLLoader.load(getClass().getResource("../resources/layouts/LoginView.fxml"));			
			Scene scene = new Scene(root,900,600);
			scene.setOnKeyPressed(k -> {
				if(k.getCode().equals(KeyCode.F11))
					primaryStage.setFullScreen(!primaryStage.fullScreenProperty().get());
			});
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());			
			primaryStage.setScene(scene);
			scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});
			Platform.setImplicitExit(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {
		launch(args);
	}
}
