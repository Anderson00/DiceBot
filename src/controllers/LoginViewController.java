package controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.NotificationSide;
import com.github.plushaze.traynotification.notification.Notifications;
import com.github.plushaze.traynotification.notification.TrayNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import application.ApplicationSingleton;
import de.jensd.fx.fontawesome.AwesomeIcon;
import de.jensd.fx.fontawesome.Icon;
import impl.org.controlsfx.skin.NotificationPaneSkin;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.RoundImageView;
import model.RoundImageViewSkin;

public class LoginViewController implements Initializable {
    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField authField;

    @FXML
    private JFXPasswordField pwdField;
    
    @FXML
    private ChoiceBox<String> choiceMode;
    
    @FXML
    private JFXCheckBox saveUserCheckBox;

    @FXML
    private Text avisoIncorreto;

    @FXML
    private JFXButton loginBtn;

    @FXML
    private JFXButton createAcountBtn;

    @FXML
    private StackPane fotoIcon;
    
    StackPane modal;

	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		
		Properties prop = System.getProperties();
		try {
			prop.loadFromXML(new FileInputStream("Prop"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String username = prop.getProperty("username");
		if(!username.equals(""))
			if(username != null && username != ""){
				authField.setText(username);
				saveUserCheckBox.setSelected(true);
			}
		
		RoundImageView imageView = new RoundImageView();
		
		RoundImageViewSkin skin = new RoundImageViewSkin(imageView);
        skin.getSkinnable().setMaxWidth(200);
        skin.getSkinnable().setMinWidth(200);
        skin.getSkinnable().setMaxHeight(150.0);
        skin.getSkinnable().setMinHeight(150.0);
        
		fotoIcon.getChildren().add(imageView);
		choiceMode.setItems(FXCollections.observableArrayList("999Dice","PrimeDice"));		
		choiceMode.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				
				URL url = getClass().getResource("../resources/icons/"+newValue+".png");
				if(url != null){
					FadeTransition transition = new FadeTransition(Duration.millis(500));
					transition.setFromValue(0.5);
					transition.setToValue(1);
					transition.setNode(imageView);
					transition.playFromStart();
					imageView.setImage(new Image(url.toString()));
				}
				else
					imageView.setImage(null);
			}
		});
		choiceMode.setValue("999Dice"); //Default		
		
		
		RequiredFieldValidator requiredUser = new RequiredFieldValidator();
		requiredUser.setIcon(new Icon(AwesomeIcon.WARNING,"0.6em",";","error"));
		requiredUser.setMessage("Required user");
		
		RequiredFieldValidator requiredPass = new RequiredFieldValidator();
		requiredPass.setIcon(new Icon(AwesomeIcon.WARNING,"0.6em",";","error"));
		requiredPass.setMessage("Required Pass");
		
		EventHandler<KeyEvent> fullScreenEvent = keyEvent -> {
			if(keyEvent.getCode().equals(KeyCode.F11)){
				stage.setFullScreen(!stage.fullScreenProperty().get());
			}
		};
		
		authField.setOnKeyPressed(fullScreenEvent);
		pwdField.setOnKeyPressed(fullScreenEvent);
		
		authField.getValidators().add(requiredUser);
        pwdField.getValidators().add(requiredPass);
		
		
	}
	
	@FXML
    void loginButtonAction(ActionEvent event) {
		if(authField.getText().equals("") || pwdField.getText().equals("")){
    		authField.validate();
    		pwdField.validate();
		}else{
			saveUserName();
			modal = new StackPane(new JFXSpinner());
			modal.setStyle("-fx-background-color: rgba(0,0,0,.5)");
			stackPane.getChildren().add(modal);
			LoginThread thread = new LoginThread(this, choiceMode.getValue(),authField.getText(),pwdField.getText());
			new Thread(thread).start();			
		}
		
		TrayNotification tray = new TrayNotification("Bet Error", "Insuficients Funds", Notifications.ERROR, NotificationSide.BOTTOM_RIGHT);
		tray.setAnimation(Animations.POPUP);
		tray.showAndDismiss(Duration.millis(5000));
    }
	
	private void saveUserName(){
		Properties prop = System.getProperties();
		if(saveUserCheckBox.isSelected()){			
			prop.setProperty("username", authField.getText());
			try {
				FileOutputStream stream = new FileOutputStream("Prop");
				prop.storeToXML(stream,"");
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			prop.setProperty("username","");
			try {
				FileOutputStream stream = new FileOutputStream("Prop");
				prop.storeToXML(stream,"");
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	void removeModal(){
		stackPane.getChildren().remove(modal);
	}
	
	void setAvisoMsg(String text){
		avisoIncorreto.setText(text);
	}
	
	public void setView(Parent parent){
		this.stackPane.getScene().setRoot(parent);
	}
	    

}
