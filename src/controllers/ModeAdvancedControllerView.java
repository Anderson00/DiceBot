package controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

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
	
	boolean stopBetting = false;
	
	private BotHeart botHeart;
	
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
    		System.out.println(newValue);
    		if(newValue.compareTo(BigDecimal.ZERO) <= 0)
    			return;
    		for(int i = 0; i <= MAX_LEVEL; i++){
    			BigDecimal aux = newValue.multiply(new BigDecimal(fibList.get(i)));
    			aux = aux.setScale(8, RoundingMode.CEILING);
    			fibonacciList.getItems().add(i+"  "+aux.toPlainString());
    		}
    		
    	});
        
        onLoss.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				System.out.println( ((RadioButton)newValue).getText() );
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
    	private int winCount = 0, levelWin;

		public PlaceBetTask2(String mode, BigDecimalField startingBet, BigDecimalField chance, ToggleGroup betType,
				JFXButton stopBtn) {
			super(mode, startingBet, chance, betType, stopBtn);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void executionMode(String mode, boolean high) throws Exception {
			// TODO Auto-generated method stub
			System.out.println(stopBetting);
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
			PlaceBetResponse betResponse = botHeart.placeBet(betType2.getToggles().get(0).equals(betType2.getSelectedToggle()), this.getStartBet(), chanceBet.getNumber().doubleValue());
			this.setBetResponse(betResponse);
			if(!betResponse.isSuccess()){
				this.setErrorBetting(true);
				this.updateValue("Bet Error");
				return;
			}
			updateProgress(System.currentTimeMillis(), 0);
			if(stopBetting){				
				this.setStopBetting(true);
				return;
			}
			Thread.sleep(100);
			
			int levelReached = levelReachedField.getNumber().intValue();
			
			if(leavelReachedCheckBox.isSelected() && (levelLosse >= levelReached || levelWin >= levelReached)){
				if(stopLevelReached_RB.isSelected()){
					stopBetting = true;
					this.setStopBetting(true);
					return;
				}else{
					levelWin = 0;
					levelLosse = 0;
					winCount = 0;
					losseCount = 0;
					this.setStartBet(this.getStartingBet().getNumber());
					return;
				}
			}
			
			
			if(betResponse.isWinner()){
				winCount++;
				losseCount = 0;
				levelLosse = 0;				
				if(resetWin.isSelected()){
					this.setStartBet(this.getStartingBet().getNumber());
				}else if(incrementWin.isSelected()){
					int n = stepOnWinField.getNumber().intValue();
					levelWin += n;
					String fibValue = fibList.get(levelWin);
					BigDecimal amount = this.getStartingBet().getNumber().multiply(new BigDecimal(fibValue));
					this.setStartBet(amount);
				}else{
					stopBetting = true;
					this.setStopBetting(true);
				}
			}else{
				losseCount++;
				winCount = 0;
				levelWin = 0;				
				if(incrementLoss.isSelected()){	
					int n = stepOnLossField.getNumber().intValue();
					levelLosse += n;					
					String fibValue = fibList.get(levelLosse);
					BigDecimal amount = this.getStartingBet().getNumber().multiply(new BigDecimal(fibValue));
					this.setStartBet(amount);					
				}else if(resetLoss.isSelected()){
					this.setStartBet(this.getStartingBet().getNumber());
				}else{
					stopBetting = true;
					this.setStopBetting(true);
				}
			}
			
		}
	}
}
