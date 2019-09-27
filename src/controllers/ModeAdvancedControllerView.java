package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.controlsfx.glyphfont.FontAwesome.Glyph;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import application.ApplicationSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.scene.control.LocalDateTimeTextField;
import model.BetTools;
import model.BotHeart;
import model.bet.PlaceBetResponse;

public class ModeAdvancedControllerView {
	
	@FXML
	private BigDecimalField startingBet, chanceBet;

	@FXML
	private JFXButton startBtn,stopBtn;
	
	@FXML
	private TabPane tabPane,tabPaneStrategies;
	
    @FXML
    private ToggleGroup betType2;
    
    @FXML
    private JFXRadioButton betTypeHigh;

    @FXML
    private JFXRadioButton betTypeLow;
	
	@FXML
	private Tab martingaleTab, labouchereTab, fibonacciTab, alembertTab, presetTab, customTab;
	
	boolean stopBetting = false, resetBetting = false;
	
	private BotHeart botHeart;
	
	//Stop condition
	@FXML
    private BigDecimalField stopLimitDecimalFeld;

    @FXML
    private BigDecimalField stopLowerLimitDecimalFeld;

    @FXML
    private JFXRadioButton withdrawLimitReached;

    @FXML
    private JFXRadioButton stopPlayingLimitReached;

    @FXML
    private JFXCheckBox limitEnabled;

    @FXML
    private JFXCheckBox lowerLimitEnabled;

    @FXML
    private JFXTextField addressField;
    
    @FXML
    private BigDecimalField withdrawAmount, resetAfterGDFbets, stopAfterGDFbets;

    @FXML
    private JFXCheckBox stopAfterGCBbets;

    @FXML
    private JFXCheckBox resetAfterGCBbets;

    @FXML
    private JFXCheckBox stopAfterGCBDate;

    @FXML
    private LocalDateTimeTextField stopAfterGLDDate;
    
    @FXML
    private JFXCheckBox resetAfterLCBinRow;

    @FXML
    private JFXCheckBox stopAfterLCBstreak;

    @FXML
    private JFXCheckBox stopAfterLCBloss;

    @FXML
    private JFXCheckBox resetAfterLCBstreak;

    @FXML
    private JFXCheckBox resetAfterLCBloss;

    @FXML
    private JFXCheckBox stopAfterLCBlosses;
    
    @FXML
    private JFXCheckBox stopAfterLCBinRow;

    @FXML
    private JFXCheckBox resetAfterLCBlosses;

    @FXML
    private BigDecimalField resetAfterLDFinRow, stopAfterLDFstreak, resetAfterLDFlosses, stopAfterLDFinRow, stopAfterLDFlosses, resetAfterLDFloss, resetAfterLDFstreak, stopAfterLDFloss;

    @FXML
    private JFXCheckBox resetAfterWCBinRow;

    @FXML
    private JFXCheckBox stopAfterWCBprofit;

    @FXML
    private JFXCheckBox resetAfterWCBstreak;

    @FXML
    private JFXCheckBox resetAfterWCBprofit;

    @FXML
    private JFXCheckBox stopAfterWCBwins;

    @FXML
    private JFXCheckBox stopAfterWCBinRow;

    @FXML
    private JFXCheckBox resetAfterWCBwins, stopAfterWCBstreak;

    @FXML
    private BigDecimalField resetAfterWDFinRow, stopAfterWDFstreak, stopAfterWDFprofit, stopAfterWDFwins, resetAfterWDFstreak, resetAfterWDFprofit, resetAfterWDFwins, stopAfterWDFinRow;

    //Labouch�re 
    
    @FXML
    private ListView<String> labListOfBets;
    
    @FXML
    private JFXButton labLoadFile, editListBTN, removeListBTN, clearListBTN;
    
    @FXML
    private JFXRadioButton labRadioStop;
    
    private File labouchereFile;
    
	
	//Fibonacci	
    @FXML
    private BigDecimalField stepOnLossField;
	
    @FXML
    private BigDecimalField stepOnWinField;

    @FXML
    private BigDecimalField levelReachedField;
    
    @FXML
    private ToggleGroup onLoss;
    
    @FXML
    private RadioButton incrementLoss, resetLoss, stopLoss;

    @FXML
    private ToggleGroup onWin;
    
    @FXML
    private RadioButton incrementWin, resetWin, stopWin;
    
    @FXML
    private ListView<String> fibonacciList;
    
    @FXML
    private JFXCheckBox leavelReachedCheckBox;
    
    @FXML
    private ToggleGroup levelReached;
    
    @FXML
    private RadioButton stopLevelReached_RB;
    
    
    //D'Alembert    
    @FXML
    private BigDecimalField incrementBetLDF, incrementBetWDF, stretchBetsLDF, stretchBetsWDF;
    
    //Advanced Settings
    @FXML
    private BigDecimalField switchAfterWDFwins, switchAfterLDFlosses, switchAfterLDFrow, switchAfterLDFbets, switchAfterWDFrow
    						, minimumBetDF, maximumBetDF;

    @FXML
    private JFXCheckBox switchAfterWCBwins, switchAfterLCBlosses, switchAfterLCBrow, switchAfterLCBbets, switchAfterWCBrow
    					, minimumBetCB, maximumBetCB;

    public static final int MAX_LEVEL = 99; // max level of fibonacci
    
    List<String> fibList = BetTools.getFibList();
    //***
    
    private enum DecimalFieldType{
    	DECIMAL_BTC, DECIMAL_BTC_NEGATIVE, DECIMAL_INTEGER, DECIMAL_INTEGER_ZERO;
    }
    
    @FXML
    void initialize() {
    	
    	editListBTN.setGraphic(new org.controlsfx.glyphfont.Glyph("FontAwesome", Glyph.EDIT));    	
    	removeListBTN.setGraphic(new org.controlsfx.glyphfont.Glyph("FontAwesome", Glyph.REMOVE));
    	clearListBTN.setGraphic(new org.controlsfx.glyphfont.Glyph("FontAwesome", Glyph.TRASH));
    	
    	botHeart = ApplicationSingleton.getInstance().getBotHeart();
    	
        startingBet.setNumber(BigDecimal.ZERO);
    	startingBet.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	startingBet.setMinValue(BigDecimal.ZERO);
    	//startingBet.setMaxValue(new BigDecimal(1)); // Depends on the balance
    	startingBet.setStepwidth(new BigDecimal(0.00000100));
    	
    	chanceBet.setNumber(new BigDecimal(49.95));// Changes depending on site
    	chanceBet.setStepwidth(new BigDecimal(0.5));
    	chanceBet.setMinValue(BigDecimal.ONE);// Changes depending on site
    	chanceBet.setMaxValue(new BigDecimal(99));// Changes depending on site
    	chanceBet.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	
    	stepOnLossField.setNumber(new BigDecimal(1));
    	stepOnLossField.setMaxValue(new BigDecimal(MAX_LEVEL));
    	stepOnLossField.setMinValue(new BigDecimal(-MAX_LEVEL));
    	
    	stepOnWinField.setNumber(BigDecimal.ZERO);
    	stepOnWinField.setMaxValue(new BigDecimal(MAX_LEVEL));
    	stepOnWinField.setMinValue(new BigDecimal(-MAX_LEVEL));
    	
    	levelReachedField.setNumber(new BigDecimal(10));
    	levelReachedField.setMaxValue(new BigDecimal(MAX_LEVEL));
    	levelReachedField.setMinValue(new BigDecimal(0));
    	
    	
    	labLoadFile.setOnAction(event -> {
    		FileChooser chooser = new FileChooser();
    		chooser.setTitle("Open text File");
    		chooser.getExtensionFilters().add(new ExtensionFilter("Text", "*.txt"));
    		File file = chooser.showOpenDialog(null);
    		labouchereFile = file;
    		labListOfBets.getItems().clear();
			try {
				InputStream stream = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				
				while(reader.ready()){					
	    			labListOfBets.getItems().add(reader.readLine());
	    		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    	});
    	
    	
    	startingBet.numberProperty().addListener((observable, oldValue, newValue) -> {
    		fibonacciList.getItems().clear();
    		if(newValue.compareTo(BigDecimal.ZERO) <= 0)
    			return;
    		for(int i = 0; i <= MAX_LEVEL; i++){
    			BigDecimal aux = newValue.multiply(new BigDecimal(fibList.get(i)));
    			aux = aux.setScale(8, RoundingMode.CEILING);
    			fibonacciList.getItems().add(i+"  "+aux.toPlainString());
    		}
    		
    	});
        
        startBtn.setOnAction(event -> {
    		stopBetting = false;
    		startBtn.setDisable(true);
	    	Thread task = new Thread(new PlaceBetTask2(tabSelected().getText(), startingBet, chanceBet, betType2, stopBtn));
	    	task.start();    		
    	});
    	
    	stopBtn.setOnAction(event -> {
    		stopBetting = true;
    		startBtn.setDisable(false);
    	});
    	
    	configureBigDecimalFields();
    }
    
    private void configureBigDecimalFields(){
    	
    	withdrawLimitReached.selectedProperty().addListener((observable, oldValue, newValue) -> {
    		if(!newValue){
    			withdrawAmount.setDisable(true);
    			addressField.setDisable(true);
    		}else{
    			withdrawAmount.setDisable(false);
    			addressField.setDisable(false);
    		}
    	});
    	
    	configureDecimalField(stopLimitDecimalFeld, DecimalFieldType.DECIMAL_BTC);    	
    	configureDecimalField(stopLowerLimitDecimalFeld, DecimalFieldType.DECIMAL_BTC);    	
    	configureDecimalField(withdrawAmount, DecimalFieldType.DECIMAL_BTC);
    	withdrawAmount.setDisable(true);
    	
    	addressField.setDisable(true);
    	
    	
    	//General Stop/reset conditions
    	configureDecimalField(stopAfterGDFbets, DecimalFieldType.DECIMAL_INTEGER);
    	
    	configureDecimalField(resetAfterGDFbets, DecimalFieldType.DECIMAL_INTEGER);
    	
    	//Stop/reset conditions on losses
    	configureDecimalField(resetAfterLDFinRow, DecimalFieldType.DECIMAL_INTEGER);    	
    	configureDecimalField(stopAfterLDFinRow, DecimalFieldType.DECIMAL_INTEGER);
    	configureDecimalField(stopAfterLDFstreak, DecimalFieldType.DECIMAL_BTC);
    	configureDecimalField(stopAfterLDFloss, DecimalFieldType.DECIMAL_BTC);
    	configureDecimalField(resetAfterLDFstreak, DecimalFieldType.DECIMAL_BTC);    	
    	configureDecimalField(resetAfterLDFloss, DecimalFieldType.DECIMAL_BTC);    	
    	configureDecimalField(stopAfterLDFlosses, DecimalFieldType.DECIMAL_INTEGER);    	
    	configureDecimalField(resetAfterLDFlosses, DecimalFieldType.DECIMAL_INTEGER);
    	
    	//Stop/reset conditions on Wins
    	configureDecimalField(resetAfterWDFinRow, DecimalFieldType.DECIMAL_INTEGER);
    	configureDecimalField(stopAfterWDFinRow, DecimalFieldType.DECIMAL_INTEGER);
    	configureDecimalField(stopAfterWDFstreak, DecimalFieldType.DECIMAL_BTC);
    	configureDecimalField(stopAfterWDFprofit, DecimalFieldType.DECIMAL_BTC);
    	configureDecimalField(resetAfterWDFstreak, DecimalFieldType.DECIMAL_BTC);
    	configureDecimalField(resetAfterWDFprofit, DecimalFieldType.DECIMAL_BTC);
    	configureDecimalField(stopAfterWDFwins, DecimalFieldType.DECIMAL_INTEGER);
    	configureDecimalField(resetAfterWDFwins, DecimalFieldType.DECIMAL_INTEGER);
    	
    	//Advanced Settings    	    	
    	configureDecimalField(minimumBetDF, DecimalFieldType.DECIMAL_BTC,new BigDecimal(1.0e-8));      	
    	configureDecimalField(maximumBetDF, DecimalFieldType.DECIMAL_BTC,new BigDecimal(1.0e-8));  
    	configureDecimalField(switchAfterWDFwins, DecimalFieldType.DECIMAL_INTEGER);
    	configureDecimalField(switchAfterWDFrow, DecimalFieldType.DECIMAL_INTEGER);    	
    	configureDecimalField(switchAfterLDFlosses, DecimalFieldType.DECIMAL_INTEGER);    	
    	configureDecimalField(switchAfterLDFrow, DecimalFieldType.DECIMAL_INTEGER);    	
    	configureDecimalField(switchAfterLDFbets, DecimalFieldType.DECIMAL_INTEGER);
    	
    	//D'alembert
    	configureDecimalField(incrementBetLDF, DecimalFieldType.DECIMAL_BTC_NEGATIVE);    	
    	configureDecimalField(stretchBetsLDF, DecimalFieldType.DECIMAL_INTEGER_ZERO);
    	configureDecimalField(incrementBetWDF, DecimalFieldType.DECIMAL_BTC_NEGATIVE);
    	incrementBetWDF.setNumber(new BigDecimal(-BotHeart.satoshi));
    	configureDecimalField(stretchBetsWDF, DecimalFieldType.DECIMAL_INTEGER_ZERO);
    	
    }
    
    private void configureDecimalField(BigDecimalField field, DecimalFieldType type){
    	switch(type){
    	case DECIMAL_BTC:
    		field.setNumber(BigDecimal.ZERO);
    		field.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    		field.setMinValue(BigDecimal.ZERO);
    		field.setStepwidth(new BigDecimal(0.00000100));
    		break;
    	case DECIMAL_BTC_NEGATIVE:
    		field.setNumber(new BigDecimal(0.00000001));
    		field.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    		field.setStepwidth(new BigDecimal(0.00000001));
    		break;
    	case DECIMAL_INTEGER:
    		field.setNumber(BigDecimal.ONE);
    		field.setMinValue(BigDecimal.ONE);
    		field.setStepwidth(BigDecimal.ONE);
    		break;
    	case DECIMAL_INTEGER_ZERO:
    		field.setNumber(BigDecimal.ZERO);
    		field.setMinValue(BigDecimal.ZERO);
    		field.setStepwidth(BigDecimal.ONE);
    		break;
    	}
    }
    
    private void configureDecimalField(BigDecimalField field, DecimalFieldType type, BigDecimal minValue){
    	configureDecimalField(field, type);
    	field.setMinValue(minValue);
    	field.setNumber(minValue);
    }
    
    public Tab tabSelected(){
    	if(martingaleTab.isSelected())
    		return martingaleTab;
    	if(labouchereTab.isSelected())
    		return labouchereTab;
    	if(fibonacciTab.isSelected())
    		return fibonacciTab;
    	if(alembertTab.isSelected())
    		return alembertTab;
    	if(presetTab.isSelected())
    		return presetTab;
    	if(customTab.isSelected())
    		return customTab;
    	return null;
    }
    
    public class PlaceBetTask2 extends controllers.PlaceBetTask{
    	
    	private int losseCount = 0, levelLosse;
    	private int winCount = 0, levelWin, fibonacciLevel;//Fibonacci mode
    	private int streakLosseCount, streakWinCount;//D'Alembert
    	private int winAux = 0, losseAux = 0, betsAux = 0, streakWinAux = 0, streakLosseAux = 0; //Advanced Conditions

		public PlaceBetTask2(String mode, BigDecimalField startingBet, BigDecimalField chance, ToggleGroup betType,
				JFXButton stopBtn) {
			super(mode, startingBet, chance, betType, stopBtn);
			// TODO Auto-generated constructor stub
		}
		
		private void stopConditions(){
			if(limitEnabled.isSelected() && BotHeart.convertToCoin(this.getBalance()).compareTo(stopLimitDecimalFeld.getNumber()) >= 0)
				if(stopPlayingLimitReached.isSelected()){
					stopBetting = true;
					this.setFinalMessage("Stoped, Balance >= "+stopLimitDecimalFeld.getNumber().setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					return;
				}else{//withdrawRadio selected
						
				}
			
			if(lowerLimitEnabled.isSelected() && BotHeart.convertToCoin(this.getBalance()).compareTo(stopLowerLimitDecimalFeld.getNumber()) <= 0)
				if(stopPlayingLimitReached.isSelected()){
					stopBetting = true;
					this.setFinalMessage("Stoped, Balance <= "+stopLimitDecimalFeld.getNumber().setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					return;
				}else{//withdrawRadio selected
						
				}
			
			//General conditions
			if(stopAfterGCBbets.isSelected() && new BigDecimal(this.getSessionNumberOfBets()).compareTo(stopAfterGDFbets.getNumber()) >= 0){
				stopBetting = true;
				this.setFinalMessage("Stoped, Number of bets >= "+stopAfterGDFbets.getNumber().toPlainString());
				return;
			}
			
			if(resetAfterGCBbets.isSelected() && this.getSessionNumberOfBets() != 0){
				if(this.getSessionNumberOfBets() % resetAfterGDFbets.getNumber().intValue() == 0){
					resetBetting = true;
					this.updateMessage("Reset, Number of bets >= "+ resetAfterGDFbets.getNumber().toPlainString());
				}
			}
			
			LocalDateTime now = LocalDateTime.now();
			if(stopAfterGCBDate.isSelected() && stopAfterGLDDate.getLocalDateTime().compareTo(now) <= 0){
				stopBetting = true;
				this.setFinalMessage("Stoped, time reached "+stopAfterGDFbets.getNumber().toPlainString());
				return;
			}
			
			
			//Stop/Reset losses Conditions
			if(resetAfterLCBinRow.isSelected() && resetAfterLDFinRow.getNumber().intValue() <= this.getStreakLose()){
				resetBetting = true;
				this.resetStreak();
				this.updateMessage("Reset, Losses in Row >= " + resetAfterLDFinRow.getNumber().toPlainString());
			}
			
			if(stopAfterLCBinRow.isSelected() && stopAfterLDFinRow.getNumber().intValue() <= this.getStreakLose()){
				stopBetting = true;
				this.setFinalMessage("Stoped, Losses in Row >= " + resetAfterLDFinRow.getNumber().toPlainString());
				return;
			}
			
			if(stopAfterLCBstreak.isSelected() && stopAfterLDFstreak.getNumber().compareTo(convertToCoin(this.getBtcStreakLoss())) <= 0){
				stopBetting = true;
				this.setFinalMessage("Stoped, BTC streak loss >= "+ stopAfterLDFstreak.getNumber().setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
				return;
			}
			
			if(stopAfterLCBloss.isSelected()){
				if(this.getSessionProfit().compareTo(BigDecimal.ZERO) <= 0 
						&& stopAfterLDFloss.getNumber().negate().compareTo(this.getSessionProfit()) >= 0){
					stopBetting = true;
					this.setFinalMessage("Stoped, BTC loss >= "+ stopAfterLDFloss.getNumber().setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					return;
				}
			}
			
			if(resetAfterLCBstreak.isSelected() && resetAfterLDFstreak.getNumber().compareTo(convertToCoin(this.getBtcStreakLoss())) <= 0){
				resetBetting = true;
				this.resetStreak();
				this.updateMessage("Reset, BTC streak loss >= " + resetAfterLDFstreak.getNumber().toPlainString());
			}
			
			if(resetAfterLCBloss.isSelected()){
				if(this.getSessionProfit().compareTo(BigDecimal.ZERO) <= 0 
						&& resetAfterLDFloss.getNumber().negate().compareTo(this.getSessionProfit()) >= 0){
					resetBetting = true;
					this.updateMessage("Reset, BTC loss >= " + resetAfterLDFloss.getNumber().toPlainString());
				}
			}
			
			if(stopAfterLCBlosses.isSelected() && stopAfterLDFlosses.getNumber().intValue() <= this.getLosses()){
				stopBetting = true;
				this.setFinalMessage("Stoped, Number of losses >= "+ stopAfterLDFlosses.getNumber().toPlainString());
				return;
			}
			
			if(resetAfterLCBlosses.isSelected() && this.getLosses() != 0){
				if(this.getLosses() % resetAfterLDFlosses.getNumber().intValue() == 0){
					resetBetting = true;
					this.setFinalMessage("Reset, Number of losses >= "+ resetAfterLDFlosses.getNumber().toPlainString());
				}
			}
					
			
			// Stop/Reset Conditions on wins
			if(resetAfterWCBinRow.isSelected() && resetAfterWDFinRow.getNumber().intValue() <= this.getStreakWin()){
				resetBetting = true;
				this.resetStreak();
				this.updateMessage("Reset, Wins in Row >= " + resetAfterWDFinRow.getNumber().toPlainString());
			}
			
			if(stopAfterWCBinRow.isSelected() && stopAfterWDFinRow.getNumber().intValue() <= this.getStreakWin()){
				stopBetting = true;
				this.setFinalMessage("Stoped, Wins in Row >= " + resetAfterWDFinRow.getNumber().toPlainString());
				return;
			}
			
			if(stopAfterWCBstreak.isSelected() && stopAfterWDFstreak.getNumber().compareTo(convertToCoin(this.getBtcStreakWin())) <= 0){
				stopBetting = true;
				this.setFinalMessage("Stoped, BTC streak win >= "+ stopAfterWDFstreak.getNumber().setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
				return;
			}
			
			if(stopAfterWCBprofit.isSelected()){
				if(stopAfterWDFprofit.getNumber().compareTo(this.getSessionProfit()) <= 0){
					stopBetting = true;
					this.setFinalMessage("Stoped, BTC win >= "+ stopAfterWDFprofit.getNumber().setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					return;
				}
			}
			
			if(resetAfterWCBstreak.isSelected() && resetAfterWDFstreak.getNumber().compareTo(convertToCoin(this.getBtcStreakWin())) <= 0){
				resetBetting = true;
				this.resetStreak();
				this.updateMessage("Reset, BTC streak win >= " + resetAfterWDFstreak.getNumber().toPlainString());
			}
			
			if(resetAfterWCBprofit.isSelected()){
				if(resetAfterWDFprofit.getNumber().compareTo(this.getSessionProfit()) <= 0){
					resetBetting = true;
					this.updateMessage("Reset, BTC win >= " + resetAfterWDFprofit.getNumber().toPlainString());
				}
			}
			
			if(stopAfterWCBwins.isSelected() && stopAfterWDFwins.getNumber().intValue() <= this.getWins()){
				stopBetting = true;
				this.setFinalMessage("Stoped, Number of wins >= "+ stopAfterWDFwins.getNumber().toPlainString());
				return;
			}
			
			if(resetAfterWCBwins.isSelected() && this.getWins() != 0){
				if(this.getWins() % resetAfterWDFwins.getNumber().intValue() == 0){
					resetBetting = true;
					this.setFinalMessage("Reset, Number of wins >= "+ resetAfterWDFwins.getNumber().toPlainString());
				}
			}
		}

		private void advancedSettings(){
			if(minimumBetCB.isSelected()){
				if(minimumBetDF.getNumber().compareTo(convertToCoin(this.getStartBet())) > 0){
					this.setStartBet(toLongInteger(minimumBetDF.getNumber()));
				}
			}
			
			if(maximumBetCB.isSelected()){
				if(maximumBetDF.getNumber().compareTo(convertToCoin(this.getStartBet())) < 0){
					this.setStartBet(toLongInteger(maximumBetDF.getNumber()));
				}
			}
			
			if(switchAfterWCBwins.isSelected() && this.getWins() != 0){
				if(this.getWins() % switchAfterWDFwins.getNumber().intValue() == 0){
					if(this.getWins() > winAux){
						winAux = this.getWins();
						switchHighLow();
					}
					
				}
			}
			
			if(switchAfterWCBrow.isSelected() && switchAfterWDFrow.getNumber().intValue() <= this.getStreakWin()){
				if(this.getStreakWin() > streakWinAux){
					streakWinAux = this.getStreakWin();
					switchHighLow();
				}
			}
			
			if(switchAfterLCBlosses.isSelected() && this.getLosses() != 0){
				if(this.getLosses() % switchAfterLDFlosses.getNumber().intValue() == 0){
					if(this.getLosses() > losseAux){
						losseAux = this.getLosses();
						switchHighLow();
					}
				}
			}
			
			if(switchAfterLCBrow.isSelected() && this.getStreakLose() != 0){
				if(this.getStreakLose() % switchAfterLDFrow.getNumber().intValue() == 0){
					switchHighLow();
				}
			}
			
			if(switchAfterLCBbets.isSelected() && this.getNumberOfBets() != 0){
				if(this.getNumberOfBets() % switchAfterLDFbets.getNumber().intValue() == 0){
					if(this.getNumberOfBets() > betsAux){
						betsAux = this.getNumberOfBets();
						switchHighLow();
					}
				}
			}
		}
		
		private void switchHighLow(){
			if(betTypeHigh.isSelected())
				betTypeLow.setSelected(true);
			else
				betTypeHigh.setSelected(true);
		}
		
		@Override
		public void executionMode(String mode, boolean high) throws Exception {
			// TODO Auto-generated method stub
			
			stopConditions();
			advancedSettings();
			
			if(stopBetting){				
				this.setStopBetting(true);
				return;
			}
			
			switch(mode.toLowerCase()){
			case "martingale":
				
				break;
			case "labouchere":
				labouchere();
				break;				
			case "fibonacci":
				fibonacci();
				break;			
			case "d'alembert":
				dAlembert();
				break;		
			case "custom":
				
				break;
			case "preset list":
				
				break;
			}
		}
		
		public void labouchere(){
			
		}
		
		public void fibonacci() throws Exception{
			if(resetBetting){
				fibonacciLevel = 0;
				winCount = 0;
				losseCount = 0;
				this.setStartBet(toLongInteger(this.getStartingBet().getNumber()));
				resetBetting = false; //Anula o status de resetado
			}
			
			PlaceBetResponse betResponse = botHeart.placeBet(betType2.getToggles().get(0).equals(betType2.getSelectedToggle()), new BigDecimal(this.getStartBet()), chanceBet.getNumber().doubleValue());
			this.setBetResponse(betResponse);
			if(!betResponse.isSuccess()){
				System.out.println("---------------------");
				System.out.println(betResponse.toString());
				System.out.println("---------------------");
				this.setErrorBetting(true);
				this.updateValue("Bet Error");
				return;
			}
			
			updateProgress(System.currentTimeMillis(), 0);
			
			if(stopBetting){				
				this.setStopBetting(true);
				return;
			}	
			
			Thread.sleep(200);//Necessario para n�o alterar valores antes de exibir no grafico e na tabela
			
			int levelReached = levelReachedField.getNumber().intValue();
			
			if(leavelReachedCheckBox.isSelected() && fibonacciLevel >= levelReached){
				if(stopLevelReached_RB.isSelected()){
					stopBetting = true;
					this.setStopBetting(true);
					return;
				}else{
					fibonacciLevel = 0;
					winCount = 0;
					losseCount = 0;
					this.setStartBet(toLongInteger(this.getStartingBet().getNumber()));
					return;
				}
			}
			
			
			if(betResponse.isWinner()){
				winCount++;
				losseCount = 0;				
				if(resetWin.isSelected()){
					fibonacciLevel = 0;
					this.setStartBet(toLongInteger(this.getStartingBet().getNumber()));					
				}else if(incrementWin.isSelected()){
					int n = stepOnWinField.getNumber().intValue();
					if(fibonacciLevel >= 0 && fibonacciLevel+n >= 0)
						fibonacciLevel += n;
					else
						fibonacciLevel = 0;
					
					String fibValue = fibList.get(fibonacciLevel);
					BigDecimal amount = this.getStartingBet().getNumber().multiply(new BigDecimal(fibValue));
					this.setStartBet(toLongInteger(amount));
				}else{
					stopBetting = true;
					this.setStopBetting(true);
				}
			}else{
				losseCount++;
				winCount = 0;				
				if(incrementLoss.isSelected()){	
					int n = stepOnLossField.getNumber().intValue();
					if(fibonacciLevel >= 0 && fibonacciLevel+n >= 0)
						fibonacciLevel += n;
					else
						fibonacciLevel = 0;
					
					String fibValue = fibList.get(fibonacciLevel);
					BigDecimal amount = this.getStartingBet().getNumber().multiply(new BigDecimal(fibValue));
					this.setStartBet(toLongInteger(amount));					
				}else if(resetLoss.isSelected()){
					fibonacciLevel = 0;
					this.setStartBet(toLongInteger(this.getStartingBet().getNumber()));
				}else{
					stopBetting = true;
					this.setStopBetting(true);
				}
			}
		}
	
		public void dAlembert() throws Exception{
			
			PlaceBetResponse betResponse = botHeart.placeBet(betType2.getToggles().get(0).equals(betType2.getSelectedToggle()), new BigDecimal(this.getStartBet()), chanceBet.getNumber().doubleValue());
			this.setBetResponse(betResponse);
			if(!betResponse.isSuccess()){
				System.out.println("---------------------");
				System.out.println(betResponse.toString());
				System.out.println("---------------------");
				this.setErrorBetting(true);
				this.updateValue("Bet Error");
				return;
			}
			
			updateProgress(System.currentTimeMillis(), 0);
			
			if(stopBetting){				
				this.setStopBetting(true);
				return;
			}	
			
			Thread.sleep(200);//Necessario para n�o alterar valores antes de exibir no grafico e na tabela
			
				
			if(betResponse.isWinner()){
				streakWinCount++;
				streakLosseCount = 0;
				
				int stretch = stretchBetsWDF.getNumber().intValue()+1;
				long decimal = 0;
				if(streakWinCount % stretch == 0){
					decimal = this.getStartBet() + toLongInteger(incrementBetWDF.getNumber());
				}else{
					decimal = this.getStartBet();
				}
				if(decimal > 0){
					this.setStartBet(decimal);
				}else{
					this.setStartBet(toLongInteger(this.getStartingBet().getNumber()));
				}
				
			}else{
				streakLosseCount++;
				streakWinCount = 0;
				
				int stretch = stretchBetsLDF.getNumber().intValue()+1;
				long decimal = 0;
				if(streakLosseCount % stretch == 0){
					decimal = this.getStartBet() + toLongInteger(incrementBetLDF.getNumber());	
				}else{
					decimal = this.getStartBet();
				}
				if(decimal > 0){
					this.setStartBet(decimal);
				}else{
					this.setStartBet(toLongInteger(this.getStartingBet().getNumber()));
				}
			}
			
		}
    }
}