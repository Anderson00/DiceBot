package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.RoundImageView;
import application.RoundImageViewSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		File file = new File("/DiceBot/src/resources/icons/999dice.png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        
		fotoIcon.getChildren().add(imageView);
		choiceMode.setItems(FXCollections.observableArrayList("999Dice","PrimeDice"));
		//choiceMode.setValue("999Dice"); //Default		
		choiceMode.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
    

}
