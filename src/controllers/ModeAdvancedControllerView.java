package controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    
    public static final int MAX_LEVEL = 99; // max level of fibonacci
    
    List<String> fibList = BetTools.getFibList();
    //***
    
    @FXML
    void initialize() {
    	
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
    	
    	stopLimitDecimalFeld.setNumber(BigDecimal.ZERO);
    	stopLimitDecimalFeld.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	stopLimitDecimalFeld.setMinValue(BigDecimal.ZERO);
    	stopLimitDecimalFeld.setStepwidth(new BigDecimal(0.00000100));
    	
    	stopLowerLimitDecimalFeld.setNumber(BigDecimal.ZERO);
    	stopLowerLimitDecimalFeld.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	stopLowerLimitDecimalFeld.setMinValue(BigDecimal.ZERO);
    	stopLowerLimitDecimalFeld.setStepwidth(new BigDecimal(0.00000100));
    	
    	withdrawAmount.setNumber(BigDecimal.ZERO);
    	withdrawAmount.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	withdrawAmount.setMinValue(BigDecimal.ZERO);
    	withdrawAmount.setStepwidth(new BigDecimal(0.00000100));
    	withdrawAmount.setDisable(true);
    	
    	addressField.setDisable(true);
    	
    	
    	//General Stop/reset conditions
    	stopAfterGDFbets.setNumber(BigDecimal.ONE);
    	stopAfterGDFbets.setMinValue(BigDecimal.ONE);
    	stopAfterGDFbets.setStepwidth(BigDecimal.ONE);
    	
    	resetAfterGDFbets.setNumber(BigDecimal.ONE);
    	resetAfterGDFbets.setMinValue(BigDecimal.ONE);
    	resetAfterGDFbets.setStepwidth(BigDecimal.ONE);
    	
    	//Stop/reset conditions on losses
    	resetAfterLDFinRow.setNumber(BigDecimal.ONE);
    	resetAfterLDFinRow.setMinValue(BigDecimal.ONE);
    	resetAfterLDFinRow.setStepwidth(BigDecimal.ONE);
    	
    	stopAfterLDFinRow.setNumber(BigDecimal.ONE);
    	stopAfterLDFinRow.setMinValue(BigDecimal.ONE);
    	stopAfterLDFinRow.setStepwidth(BigDecimal.ONE);
    	
    	stopAfterLDFstreak.setNumber(BigDecimal.ZERO);
    	stopAfterLDFstreak.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	stopAfterLDFstreak.setMinValue(BigDecimal.ZERO);
    	stopAfterLDFstreak.setStepwidth(new BigDecimal(0.00000100));
    	
    	stopAfterLDFloss.setNumber(BigDecimal.ZERO);
    	stopAfterLDFloss.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	stopAfterLDFloss.setMinValue(BigDecimal.ZERO);
    	stopAfterLDFloss.setStepwidth(new BigDecimal(0.00000100));
    	
    	resetAfterLDFstreak.setNumber(BigDecimal.ZERO);
    	resetAfterLDFstreak.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	resetAfterLDFstreak.setMinValue(BigDecimal.ZERO);
    	resetAfterLDFstreak.setStepwidth(new BigDecimal(0.00000100));
    	
    	resetAfterLDFloss.setNumber(BigDecimal.ZERO);
    	resetAfterLDFloss.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	resetAfterLDFloss.setMinValue(BigDecimal.ZERO);
    	resetAfterLDFloss.setStepwidth(new BigDecimal(0.00000100));
    	
    	stopAfterLDFlosses.setNumber(BigDecimal.ONE);
    	stopAfterLDFlosses.setMinValue(BigDecimal.ONE);
    	stopAfterLDFlosses.setStepwidth(BigDecimal.ONE);
    	
    	resetAfterLDFlosses.setNumber(BigDecimal.ONE);
    	resetAfterLDFlosses.setMinValue(BigDecimal.ONE);
    	resetAfterLDFlosses.setStepwidth(BigDecimal.ONE);
    	
    	//Stop/reset conditions on Wins
    	resetAfterWDFinRow.setNumber(BigDecimal.ONE);
    	resetAfterWDFinRow.setMinValue(BigDecimal.ONE);
    	resetAfterWDFinRow.setStepwidth(BigDecimal.ONE);
    	
    	stopAfterWDFinRow.setNumber(BigDecimal.ONE);
    	stopAfterWDFinRow.setMinValue(BigDecimal.ONE);
    	stopAfterWDFinRow.setStepwidth(BigDecimal.ONE);
    	
    	stopAfterWDFstreak.setNumber(BigDecimal.ZERO);
    	stopAfterWDFstreak.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	stopAfterWDFstreak.setMinValue(BigDecimal.ZERO);
    	stopAfterWDFstreak.setStepwidth(new BigDecimal(0.00000100));
    	
    	stopAfterWDFprofit.setNumber(BigDecimal.ZERO);
    	stopAfterWDFprofit.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	stopAfterWDFprofit.setMinValue(BigDecimal.ZERO);
    	stopAfterWDFprofit.setStepwidth(new BigDecimal(0.00000100));
    	
    	resetAfterWDFstreak.setNumber(BigDecimal.ZERO);
    	resetAfterWDFstreak.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	resetAfterWDFstreak.setMinValue(BigDecimal.ZERO);
    	resetAfterWDFstreak.setStepwidth(new BigDecimal(0.00000100));
    	
    	resetAfterWDFprofit.setNumber(BigDecimal.ZERO);
    	resetAfterWDFprofit.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	resetAfterWDFprofit.setMinValue(BigDecimal.ZERO);
    	resetAfterWDFprofit.setStepwidth(new BigDecimal(0.00000100));
    	
    	stopAfterWDFwins.setNumber(BigDecimal.ONE);
    	stopAfterWDFwins.setMinValue(BigDecimal.ONE);
    	stopAfterWDFwins.setStepwidth(BigDecimal.ONE);
    	
    	resetAfterWDFwins.setNumber(BigDecimal.ONE);
    	resetAfterWDFwins.setMinValue(BigDecimal.ONE);
    	resetAfterWDFwins.setStepwidth(BigDecimal.ONE);
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
    	private int winCount = 0, levelWin, fibonacciLevel;

		public PlaceBetTask2(String mode, BigDecimalField startingBet, BigDecimalField chance, ToggleGroup betType,
				JFXButton stopBtn) {
			super(mode, startingBet, chance, betType, stopBtn);
			// TODO Auto-generated constructor stub
		}
		
		private void stopConditions(){
			if(limitEnabled.isSelected() && this.getBalance().compareTo(stopLimitDecimalFeld.getNumber()) >= 0)
				if(stopPlayingLimitReached.isSelected()){
					stopBetting = true;
					this.setFinalMessage("Stoped, Balance >= "+stopLimitDecimalFeld.getNumber().setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					return;
				}else{//withdrawRadio selected
						
				}
			
			if(lowerLimitEnabled.isSelected() && this.getBalance().compareTo(stopLowerLimitDecimalFeld.getNumber()) <= 0)
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
			
			if(resetAfterGCBbets.isSelected()){
				if(this.getSessionNumberOfBets() == 0){
					//Não faz nada
				}				
				else if(this.getSessionNumberOfBets() % resetAfterGDFbets.getNumber().intValue() == 0){
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
			
			if(stopAfterLCBstreak.isSelected() && stopAfterLDFstreak.getNumber().compareTo(this.getBtcStreakLoss()) <= 0){
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
			
			if(resetAfterLCBstreak.isSelected() && resetAfterLDFstreak.getNumber().compareTo(this.getBtcStreakLoss()) <= 0){
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
			
			if(resetAfterLCBlosses.isSelected()){
				if(this.getLosses() == 0){
					//Não faz nada
				}
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
			
			if(stopAfterWCBstreak.isSelected() && stopAfterWDFstreak.getNumber().compareTo(this.getBtcStreakWin()) <= 0){
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
			
			if(resetAfterWCBstreak.isSelected() && resetAfterWDFstreak.getNumber().compareTo(this.getBtcStreakWin()) <= 0){
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
			
			if(stopAfterWCBwins.isSelected() && stopAfterWDFwins.getNumber().intValue() <= this.getLosses()){
				stopBetting = true;
				this.setFinalMessage("Stoped, Number of wins >= "+ stopAfterWDFwins.getNumber().toPlainString());
				return;
			}
			
			if(resetAfterWCBwins.isSelected()){
				if(this.getLosses() == 0){
					//Não faz nada
				}
				if(this.getLosses() % resetAfterWDFwins.getNumber().intValue() == 0){
					resetBetting = true;
					this.setFinalMessage("Reset, Number of wins >= "+ resetAfterWDFwins.getNumber().toPlainString());
				}
			}
		}

		@Override
		public void executionMode(String mode, boolean high) throws Exception {
			// TODO Auto-generated method stub
			
			stopConditions();
			
			if(stopBetting){				
				this.setStopBetting(true);
				return;
			}
			
			switch(mode.toLowerCase()){
			case "martingale":
				
				break;
			case "labouchère":
				
				break;				
			case "fibonacci":
				fibonacci();
				break;			
			case "d´alembert":
				System.out.println("d´alembert");
				break;			
			case "preset list":
				
				break;
			}
		}   
		
		public void fibonacci() throws Exception{
			if(resetBetting){
				fibonacciLevel = 0;
				winCount = 0;
				losseCount = 0;
				this.setStartBet(this.getStartingBet().getNumber());
				resetBetting = false; //Anula o status de resetado
			}
			
			PlaceBetResponse betResponse = botHeart.placeBet(betType2.getToggles().get(0).equals(betType2.getSelectedToggle()), this.getStartBet(), chanceBet.getNumber().doubleValue());
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
			
			Thread.sleep(200);//Necessario para não alterar valores antes de exibir no grafico e na tabela
			
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
					this.setStartBet(this.getStartingBet().getNumber());
					return;
				}
			}
			
			
			if(betResponse.isWinner()){
				winCount++;
				losseCount = 0;				
				if(resetWin.isSelected()){
					fibonacciLevel = 0;
					this.setStartBet(this.getStartingBet().getNumber());					
				}else if(incrementWin.isSelected()){
					int n = stepOnWinField.getNumber().intValue();
					if(fibonacciLevel >= 0 && fibonacciLevel+n >= 0)
						fibonacciLevel += n;
					else
						fibonacciLevel = 0;
					
					String fibValue = fibList.get(fibonacciLevel);
					BigDecimal amount = this.getStartingBet().getNumber().multiply(new BigDecimal(fibValue));
					this.setStartBet(amount);
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
					this.setStartBet(amount);					
				}else if(resetLoss.isSelected()){
					fibonacciLevel = 0;
					this.setStartBet(this.getStartingBet().getNumber());
				}else{
					stopBetting = true;
					this.setStopBetting(true);
				}
			}
			
			
			
			
		}
	}
}
