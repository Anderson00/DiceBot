package application;

import controllers.LoginViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.dao.UserJpaDAO;


public class Main extends Application {	
	
	@Override
	public void start(Stage primaryStage) {
		try {			
			UserJpaDAO.getInstance();//Initialize Hibernate and Hsqldb
			ApplicationSingleton.getInstance().setApplication(this);// init singleton	
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/layouts/LoginView.fxml"));			
			StackPane root = loader.load();
			LoginViewController loginController = loader.getController();
			loginController.setStage(primaryStage);
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
