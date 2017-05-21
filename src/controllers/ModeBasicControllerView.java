package controllers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.jfoenix.controls.JFXButton;

import application.ApplicationSingleton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.BigDecimalField;
import model.Bet;
import sites.client999dice.BeginSessionResponse;
import sites.client999dice.DiceWebAPI;
import sites.client999dice.PlaceBetResponse;
import sites.client999dice.SessionInfo;

public class ModeBasicControllerView {
	
	//public final double umSatoshi = 0.00000001;
	public final double oneSatoshi = 1.0e-8;
	
	@FXML
    private JFXButton startBtn, stopBtn;

    @FXML
    private BigDecimalField startingBet, chanceToWin,onLoss, onWin;

    @FXML
    private ToggleGroup betType;    
    
    boolean stopBetting = false;
    private int count = 0;
    private double sessionProfit;
    
    @FXML
    void initialize() {
    	
    	ApplicationSingleton.getInstance().setModeBasicController(this);
    	
    	startingBet.setNumber(BigDecimal.ZERO);
    	startingBet.setFormat(new DecimalFormat("#,########0.00000000"));
    	startingBet.setMinValue(BigDecimal.ZERO);
    	//startingBet.setMaxValue(new BigDecimal(1)); // Depends on the balance
    	startingBet.setStepwidth(new BigDecimal(0.00000100));
    	
    	chanceToWin.setNumber(new BigDecimal(50));// Changes depending on site
    	chanceToWin.setStepwidth(BigDecimal.ONE);
    	chanceToWin.setMinValue(BigDecimal.ONE);// Changes depending on site
    	chanceToWin.setMaxValue(new BigDecimal(99));// Changes depending on site
    	chanceToWin.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));

    	onLoss.setNumber(BigDecimal.ONE);
    	onLoss.setStepwidth(BigDecimal.ONE);
    	onLoss.setMinValue(BigDecimal.ONE);
    	onLoss.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));

    	onWin.setNumber(BigDecimal.ONE);
    	onWin.setStepwidth(BigDecimal.ONE);
    	onWin.setMinValue(BigDecimal.ONE);
    	onWin.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	
    	
    	
    	
    	startBtn.setOnAction(event -> {
    		stopBetting = false;
    		Thread task = new Thread(new PlaceBetTask());
    		task.start();
    	});
    	
    	stopBtn.setOnAction(event -> {
    		stopBetting = true;
    	});
    }
    
    public class PlaceBetTask extends Task<Boolean>{
    	
    	PlaceBetResponse betResponse = null;
    	BigDecimal startBet = startingBet.getNumber();
    	boolean executed = true;

		@Override
		protected Boolean call() throws Exception {
			// TODO Auto-generated method stub
			BeginSessionResponse session = ApplicationSingleton.getInstance().getSession();
			
			while(true && !stopBetting){
				SessionInfo info = session.getSession();				
				betResponse = DiceWebAPI.PlaceBet(info, startBet, 0, 499499);		
				if(betResponse.getPayOut().compareTo(BigDecimal.ZERO) == 0){
					startBet = startBet.multiply(onLoss.getNumber());
					System.out.println(">> "+startBet);
				}else{
					//reset bet
					startBet = startingBet.getNumber();
					startBet = startBet.multiply(onWin.getNumber());
				}
				Thread.sleep(500);
				
				updateProgress(0, 0);
				System.out.println(betResponse.isSuccess()+" "+betResponse.getPayOut()+" "+betResponse.getStartingBalance());
				
				
			}
			return null;
		}
		@Override
		protected void updateProgress(double workDone, double max) {
			// TODO Auto-generated method stub
			super.updateProgress(workDone, max);
			
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(betResponse.isSuccess()){
						HomeControllerView controller = ApplicationSingleton.getInstance().getHomeController();
						BeginSessionResponse session = ApplicationSingleton.getInstance().getSession();
						BigDecimal profit = (betResponse.getPayOut().compareTo(BigDecimal.ZERO) == 0)? startBet.negate() : betResponse.getPayOut().subtract(startBet);
						System.out.println(">>>>> "+profit.toPlainString());
						sessionProfit += profit.doubleValue();
						System.out.println(profit.doubleValue());
						BigDecimal balance = (betResponse.getPayOut().compareTo(BigDecimal.ZERO) == 0)? betResponse.getStartingBalance().subtract(startingBet.getNumber()) : betResponse.getPayOut().subtract(startingBet.getNumber()).add(betResponse.getStartingBalance());
						boolean win = (betResponse.getPayOut().compareTo(BigDecimal.ZERO) == 0)? false : true; 
						
						controller.topBalance.setText(balance.toString());
						controller.balanceLB.setText(balance.toString());
						controller.tableBets.getItems().add(0, new Bet.BetBuilder(betResponse.getBetId(), 50, startBet.toPlainString(),win)
								.high(betType.getToggles().get(0).equals(betType.getSelectedToggle()))
								.profit(profit.toPlainString())
								.build());
						
						System.out.println(">> "+betResponse.getPayOut().toPlainString());
						controller.chartBets.getData().get(0).getData().add(new XYChart.Data<Number,Number>(count, sessionProfit));
						count++;
						executed = true;
					}
				}
			});
			
		}
    	
    }
}
