package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.fontawesome.AwesomeIcon;
import de.jensd.fx.fontawesome.Icon;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.RoundImageView;
import model.RoundImageViewSkin;
import sites.client999dice.DiceWebAPI;

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
    private Text avisoIncorreto;

    @FXML
    private JFXButton loginBtn;

    @FXML
    private JFXButton createAcountBtn;

    @FXML
    private StackPane fotoIcon;
    
    StackPane modal;

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
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
		
		authField.getValidators().add(requiredUser);
        pwdField.getValidators().add(requiredPass);
		
		
	}
	
	@FXML
    void loginButtonAction(ActionEvent event) {
		if(authField.getText().equals("") || pwdField.getText().equals("")){
    		authField.validate();
    		pwdField.validate();
		}else{
			modal = new StackPane(new JFXSpinner());
			modal.setStyle("-fx-background-color: rgba(0,0,0,.5)");
			stackPane.getChildren().add(modal);
			LoginThread thread = new LoginThread(this, choiceMode.getValue(),authField.getText(),pwdField.getText());
			new Thread(thread).start();			
		}
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
