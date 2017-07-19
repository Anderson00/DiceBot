package controllers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXRippler.RipplerMask;
import com.jfoenix.controls.JFXRippler.RipplerPos;

import application.ApplicationSingleton;
import exceptions.ErrorsList;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import jfxtras.labs.scene.control.BigDecimalField;
import model.Bet;
import model.BotHeart;
import model.bet.BeginSessionResponse;
import model.bet.PlaceBetResponse;
import model.bet.SessionInfo;

public class ModeBasicControllerView {
	
	//public final double umSatoshi = 0.00000001;
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
    private int count = 0;
    private double sessionProfit;
    private long winsCount = 0, lossesCount = 0, betCount = 0;
    private BotHeart botHeart;
    
    @FXML
    void initialize() {
    	SessionInfo info = ApplicationSingleton.getInstance().getBotHeart().getSession().getSession();
    	winsCount = info.getBetWinCount();
    	betCount = info.getBetCount();
    	lossesCount = betCount - winsCount;
    	
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
    		Thread task = new Thread(new PlaceBetTask2("", startingBet, chanceToWin, betType, stopBtn));
	    	task.start();    		
    	});
    	
    	stopBtn.setOnAction(event -> {
    		stopBetting = true;
    		startBtn.setDisable(false);
    	});
    }
    
    public class PlaceBetTask2 extends controllers.PlaceBetTask{

		public PlaceBetTask2(String mode, BigDecimalField startingBet, BigDecimalField chance, ToggleGroup betType,
				JFXButton stopBtn) {
			super(mode, startingBet, chance, betType, stopBtn);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void executionMode(String mode, boolean high) throws Exception{
			// TODO Auto-generated method stub

			PlaceBetResponse betResponse = botHeart.placeBet(betType.getToggles().get(0).equals(betType.getSelectedToggle()), this.getStartBet(), chanceToWin.getNumber().doubleValue());
			this.setBetResponse(betResponse);
			if(!betResponse.isSuccess()){
				this.updateValue("Bet Error");
				return;
			}
			this.incrementNumberOfBets();
			updateProgress(System.currentTimeMillis(), 0);
			if(stopBetting){
				this.setStopBetting(true);
				return;
			}
			Thread.sleep(100);
			if(!betResponse.isWinner()){
				this.setStartBet(this.getStartBet().multiply(onLoss.getNumber()));
			}else{
				//reset bet
				this.setStartBet(this.getStartingBet().getNumber());
				this.setStartBet(this.getStartBet().multiply(onWin.getNumber()));
			}	
			this.setStartBet(this.getStartBet().setScale(8, BigDecimal.ROUND_CEILING));
		}
		
		@Override
		protected void updateProgress(long workDone, long max) {
			// TODO Auto-generated method stub
			super.updateProgress(workDone, max);
			
			System.out.println(">>>>> "+getSessionProfit());
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					nBetsSession.setText(getNumberOfBets()+"");
					winsSession.setText(getWins()+"");
					lossesSession.setText(getLosses()+"");
					profitSession.setText(new BigDecimal(getProfitSes()).setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
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
			});
		}
    	
    }
    
    public class PlaceBetTask extends Task<String>{
    	
    	PlaceBetResponse betResponse = null;
    	BigDecimal startBet = startingBet.getNumber().setScale(8, BigDecimal.ROUND_CEILING);    	
    	//BigDecimal startBet = startingBet.getNumber();
    	HomeControllerView controller = ApplicationSingleton.getInstance().getHomeController();
    	boolean executed = true;
    	int wins,losses,numberOfBets,currentStreak,streakWin,streakLose;
    	double profitSes;
    	
		@Override
		protected String call() throws Exception {
			// TODO Auto-generated method stub
			
			initiated = true;
			BeginSessionResponse session = ApplicationSingleton.getInstance().getBotHeart().refreshSession();		
			if(session == null){
				return ErrorsList.CONNECTION_ERROR;
			}
			
			//Check balance
			if(botHeart.getSession().getSession().getBalance().compareTo(startBet) <= 0){			
    			return ErrorsList.INSUFFICIENT_FUNDS;
    		}
			
			if(count == 0){
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ApplicationSingleton.getInstance().getHomeController().chartBets.getData().get(0).getData().add(new XYChart.Data<Number,Number>(0,0));			
					}
				});
    		}
			
			winsCount = session.getSession().getBetWinCount();
			betCount = session.getSession().getBetCount();
			lossesCount = betCount - winsCount;
			
			//updateValue("Start "+Calendar.getInstance().getTime());
			updateMessage("Start "+Calendar.getInstance().getTime());
			SessionInfo info = session.getSession();
			
			while(true){				
				if(stopBetting){ 
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis(System.currentTimeMillis());
					return "Finished "+date.getTime();				
				}
				if(startBet.compareTo(BigDecimal.ZERO) == 0) return ErrorsList.INSUFFICIENT_FUNDS;
				
				betResponse = botHeart.placeBet(betType.getToggles().get(0).equals(betType.getSelectedToggle()), startBet, chanceToWin.getNumber().doubleValue());
				if(!betResponse.isSuccess()) return "Bet Error";
				numberOfBets++;
				updateProgress(System.currentTimeMillis(), 0);
				if(stopBetting){
					continue;
				}
				Thread.sleep(100);
				if(!betResponse.isWinner()){
					startBet = startBet.multiply(onLoss.getNumber());
					System.out.println(">> "+startBet);
				}else{
					//reset bet
					startBet = startingBet.getNumber();
					startBet = startBet.multiply(onWin.getNumber());
				}	
				startBet = startBet.setScale(8, BigDecimal.ROUND_CEILING);				
			}
		}
		
		@Override
		protected void updateValue(String value) {
			// TODO Auto-generated method stub
			super.updateValue(value);	
			Platform.runLater(() ->{				
				stopBtn.fire();
				ApplicationSingleton.getInstance().getHomeController().infoLB.setText((value == null)? "" : value);
			});			
		}
		
		@Override
		protected void updateMessage(String message) {
			// TODO Auto-generated method stub
			super.updateMessage(message);
			Platform.runLater(() ->{
				ApplicationSingleton.getInstance().getHomeController().infoLB.setText((message == null)? "" : message);				
			});
		}
		
		@Override
		protected void updateProgress(long workDone, long max) {
			// TODO Auto-generated method stub
			super.updateProgress(workDone, max);
			
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub		
					System.out.println(">>>\t"+betResponse.isNoPossibleProfit());
					if(betResponse.isSuccess()){						
						BigDecimal profit = betResponse.getProfit();
						System.out.println(">>"+profit.toPlainString());
						sessionProfit += profit.doubleValue();
						profitSes += profit.doubleValue();
						
						BigDecimal balance = betResponse.getBalance().setScale(8, BigDecimal.ROUND_CEILING);
						boolean win = betResponse.isWinner(); 
						
						controller.topBalance.setText(balance.toPlainString());
						controller.balanceLB.setText(balance.toPlainString());
						
						Calendar date = Calendar.getInstance();
						date.setTimeInMillis(workDone);
						controller.tableBets.getItems().add(0, new Bet.BetBuilder(betResponse.getBetId(), chanceToWin.getNumber().setScale(2, BigDecimal.ROUND_DOWN).toPlainString(), startBet.setScale(8, BigDecimal.ROUND_CEILING).toPlainString(),win)
								.date(date.getTime().toString())
								.high(betType.getToggles().get(0).equals(betType.getSelectedToggle()))
								.roll(betResponse.getRollNumber())
								.profit(profit.setScale(8, BigDecimal.ROUND_CEILING).toPlainString())
								.build());
						boolean b = false;
						if(win){
							winsCount++;
							wins++;			
							streakLose = 0;
							streakWin++;
							controller.winsLB.setText(winsCount+"");
						}else{
							lossesCount++;
							losses++;
							streakWin = 0;
							streakLose++;
							controller.lossesLB.setText(lossesCount+"");
						}						
						System.out.println("--------- "+betResponse.getRequest().isHigh()+"");
						controller.totalBetsLB.setText(++betCount+"");	

						BigDecimal prof = botHeart.getSession().getSession().getProfit();
						
						controller.profitLB.setText(prof.setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
						controller.wageredLB.setText(botHeart.getSession().getSession().getWagered().toPlainString());
						
						System.out.println(">> "+betResponse.getProfit().toPlainString());	
						controller.chartBets.getData().get(0).getData().add(new XYChart.Data<Number,Number>(++count, sessionProfit));
						
						
						nBetsSession.setText(numberOfBets+"");
						winsSession.setText(wins+"");
						lossesSession.setText(losses+"");
						profitSession.setText(new BigDecimal(profitSes).setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
						if(streakWin > 0 || streakLose > 0)
							if(streakWin > 0){
								currentStreakSession.setStyle("-fx-text-fill:#00ff00");
								currentStreakSession.setText(streakWin+"");								
							}else{
								currentStreakSession.setStyle("-fx-text-fill:red");
								currentStreakSession.setText(streakLose+"");	
							}
						else
							currentStreakSession.setStyle("-fx-text-fill:#ccc");
						
						
					}
				}
			});
			
		}
		
		private void stop(boolean condition){
			if(condition){
				Calendar date = Calendar.getInstance();
				date.setTimeInMillis(System.currentTimeMillis());
				updateValue("Finished "+date.getTime());
				this.cancel();
			}
		}
    	
    }
}
