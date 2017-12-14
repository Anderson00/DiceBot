package controllers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.jfoenix.controls.JFXButton;

import application.ApplicationSingleton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import jfxtras.labs.scene.control.BigDecimalField;
import model.BotHeart;
import model.bet.PlaceBetResponse;

public class ModeBasicControllerView {
	
	public final double oneSatoshi = 1.0e-8;
	
	@FXML
    private JFXButton startBtn, stopBtn;

    @FXML
    private BigDecimalField startingBet, chanceToWin,onLoss, onWin;

    @FXML
    private ToggleGroup betType;    
    
    @FXML
    private Label profitSession;

    @FXML
    private Label nBetsSession;

    @FXML
    private Label winsSession;

    @FXML
    private Label lossesSession;

    @FXML
    private Label currentStreakSession;
    
    boolean stopBetting = false,initiated = false;
    private BotHeart botHeart;
    
    @FXML
    void initialize() {
    	    	
    	ApplicationSingleton.getInstance().setModeBasicController(this);
    	botHeart = ApplicationSingleton.getInstance().getBotHeart();
    	
    	startingBet.setNumber(BigDecimal.ZERO);
    	startingBet.setFormat(new DecimalFormat("#,########0.00000000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	startingBet.setMinValue(BigDecimal.ZERO);
    	//startingBet.setMaxValue(new BigDecimal(1)); // Depends on the balance
    	startingBet.setStepwidth(new BigDecimal(0.00000100));
    	
    	chanceToWin.setNumber(new BigDecimal(49.95));// Changes depending on site
    	chanceToWin.setStepwidth(new BigDecimal(0.5));
    	chanceToWin.setMinValue(BigDecimal.ONE);// Changes depending on site
    	chanceToWin.setMaxValue(new BigDecimal(99));// Changes depending on site
    	chanceToWin.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));

    	onLoss.setNumber(new BigDecimal(2));
    	onLoss.setStepwidth(BigDecimal.ONE);
    	onLoss.setMinValue(BigDecimal.ONE);
    	onLoss.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));

    	onWin.setNumber(BigDecimal.ONE);
    	onWin.setStepwidth(BigDecimal.ONE);
    	onWin.setMinValue(BigDecimal.ONE);
    	onWin.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	
    	
    	profitSession.textProperty().addListener( (observable, oldValue, newValue) -> {
			BigDecimal profit = new BigDecimal(newValue);
			int compare = profit.compareTo(BigDecimal.ZERO);
			if(compare > 0)
				profitSession.setStyle("-fx-text-fill:#00ff00");
			else if(compare == 0)
				profitSession.setStyle("-fx-text-fill:#ccc");
			else
				profitSession.setStyle("-fx-text-fill:red");
		} );
    	
    	
    	startBtn.setOnAction(event -> {
    		stopBetting = false;
    		startBtn.setDisable(true);
	    	//Thread task = new Thread(new PlaceBetTask());
    		Thread task = new Thread(new PlaceBetTask2(startingBet, chanceToWin, betType, stopBtn));
	    	task.start();    		
    	});
    	
    	stopBtn.setOnAction(event -> {
    		stopBetting = true;
    		startBtn.setDisable(false);
    	});
    }
    
    public class PlaceBetTask2 extends controllers.PlaceBetTask{
    	
    	private BigDecimal profit = BigDecimal.ZERO;
    	private boolean pastBet = false;//false = losse

		public PlaceBetTask2(String mode, BigDecimalField startingBet, BigDecimalField chance, ToggleGroup betType,
				JFXButton stopBtn) {
			super(mode, startingBet, chance, betType, stopBtn);
			// TODO Auto-generated constructor stub
		}
		
		public PlaceBetTask2(BigDecimalField startingBet, BigDecimalField chance, ToggleGroup betType, JFXButton stopBtn){
			super("basicmode",startingBet,chance,betType,stopBtn);
		}

		@Override
		public void executionMode(String mode, boolean high) throws Exception{
			// TODO Auto-generated method stub

			PlaceBetResponse betResponse = botHeart.placeBet(betType.getToggles().get(0).equals(betType.getSelectedToggle()), new BigDecimal(this.getStartBet()), chanceToWin.getNumber().doubleValue());
			this.setBetResponse(betResponse);
			System.out.println(">>>>>>>>>>>>>>>>>> " + betResponse.isSuccess());
			if(!betResponse.isSuccess()){
				this.setErrorBetting(true);
				this.updateValue("Bet Error");
				return;
			}
			profit = profit.add(betResponse.getProfit());
			this.incrementNumberOfBets();
			updateProgress(System.currentTimeMillis(), 0);
			if(stopBetting){				
				this.setStopBetting(true);
				return;
			}
			Thread.sleep(100);
			if(!betResponse.isWinner()){
				BigDecimal decimal = BotHeart.convertToCoin(this.getStartBet()).multiply(onLoss.getNumber(), MathContext.DECIMAL128);
				this.setStartBet(BotHeart.toLongInteger(decimal));
				pastBet = false;
			}else{
				if(!pastBet){
					this.setStartBet(BotHeart.toLongInteger(this.getStartingBet().getNumber()));
				}else{
					BigDecimal decimal = BotHeart.convertToCoin(this.getStartBet()).multiply(onWin.getNumber(), MathContext.DECIMAL128);
					this.setStartBet(BotHeart.toLongInteger(decimal));
				}
				pastBet = true;
			}	
			
		}
		
		@Override
		protected void updateProgress(long workDone, long max) {
			// TODO Auto-generated method stub
			super.updateProgress(workDone, max);
			
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					if(getBetResponse().isSuccess()){
						nBetsSession.setText(getNumberOfBets()+"");
						winsSession.setText(getWins()+"");
						lossesSession.setText(getLosses()+"");
						profitSession.setText(BotHeart.convertToCoin(profit).toPlainString());
						if(getStreakWin() > 0 || getStreakLose() > 0)
							if(getStreakWin() > 0){
								currentStreakSession.setStyle("-fx-text-fill:#00ff00");
								currentStreakSession.setText(getStreakWin()+"");								
							}else{
								currentStreakSession.setStyle("-fx-text-fill:red");
								currentStreakSession.setText(getStreakLose()+"");	
							}
						else
							currentStreakSession.setStyle("-fx-text-fill:#ccc");
					}
				}
			});
		}
    	
    }
}
