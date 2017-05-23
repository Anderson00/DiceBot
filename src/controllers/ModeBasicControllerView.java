package controllers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;


import com.jfoenix.controls.JFXButton;

import application.ApplicationSingleton;
import exceptions.ErrorsList;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleGroup;
import jfxtras.labs.scene.control.BigDecimalField;
import model.Bet;
import model.BotHeart;
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
    
    boolean stopBetting = false,initiated = false;
    private int count = 0;
    private double sessionProfit;
    private long winsCount = 0, lossesCount = 0, betCount = 0;
    private BotHeart botHeart;
    
    @FXML
    void initialize() {
    	/*SessionInfo info = ApplicationSingleton.getInstance().getBotHeart().getSession().getSession();
    	winsCount = info.getBetWinCount();
    	betCount = info.getBetCount();
    	lossesCount = betCount - winsCount;*/
    	SessionInfo info = ApplicationSingleton.getInstance().getBotHeart().getSession().getSession();
    	winsCount = info.getBetWinCount();
    	betCount = info.getBetCount();
    	lossesCount = betCount - winsCount;
    	
    	ApplicationSingleton.getInstance().setModeBasicController(this);
    	botHeart = ApplicationSingleton.getInstance().getBotHeart();
    	
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

    	onLoss.setNumber(new BigDecimal(2));
    	onLoss.setStepwidth(BigDecimal.ONE);
    	onLoss.setMinValue(BigDecimal.ONE);
    	onLoss.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));

    	onWin.setNumber(BigDecimal.ONE);
    	onWin.setStepwidth(BigDecimal.ONE);
    	onWin.setMinValue(BigDecimal.ONE);
    	onWin.setFormat(new DecimalFormat("#,###0.000", new DecimalFormatSymbols(Locale.ENGLISH)));
    	
    	
    	
    	
    	startBtn.setOnAction(event -> {
    		stopBetting = false;
    		if(!initiated){
	    		Thread task = new Thread(new PlaceBetTask());
	    		task.start();
    		}
    	});
    	
    	stopBtn.setOnAction(event -> {
    		initiated = false;
    		stopBetting = true;
    	});
    }
    
    public class PlaceBetTask extends Task<String>{
    	
    	PlaceBetResponse betResponse = null;
    	BigDecimal startBet = startingBet.getNumber().setScale(8, BigDecimal.ROUND_CEILING);    	
    	//BigDecimal startBet = startingBet.getNumber();
    	HomeControllerView controller = ApplicationSingleton.getInstance().getHomeController();
    	boolean executed = true;
    	
    	public PlaceBetTask() {
			// TODO Auto-generated constructor stub
    		if(count == 0)
    			ApplicationSingleton.getInstance().getHomeController().chartBets.getData().get(0).getData().add(new XYChart.Data<Number,Number>(0,0));
		}

		@Override
		protected String call() throws Exception {
			// TODO Auto-generated method stub
			
			initiated = true;
			BeginSessionResponse session = ApplicationSingleton.getInstance().getBotHeart().refreshSession();		
			if(session == null){
				return ErrorsList.CONNECTION_ERROR;
			}
			
			//Check balance
			if(count == 0 && botHeart.getSession().getSession().getBalance().compareTo(startBet) <= 0){			
    			return ErrorsList.INSUFFICIENT_FUNDS;
    		}
			
			
			System.out.println(session.getSession().getBalance());
			
			winsCount = session.getSession().getBetWinCount();
			betCount = session.getSession().getBetCount();
			lossesCount = betCount - winsCount;
			
			updateValue("Start "+Calendar.getInstance().getTime());
			
			while(true){
				SessionInfo info = session.getSession();			
				//Thread.sleep(400);
				if(stopBetting){ 
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis(System.currentTimeMillis());
					return "Finished "+date.getTime();				
				}
				if(startBet.compareTo(BigDecimal.ZERO) == 0) return ErrorsList.INSUFFICIENT_FUNDS;
				
				betResponse = DiceWebAPI.PlaceBet(info, startBet, 0, 499499);
				if(!betResponse.isSuccess()) return "Bet Error";
				updateProgress(System.currentTimeMillis(), 0);
				Thread.sleep(100);
				if(betResponse.getPayOut().compareTo(BigDecimal.ZERO) == 0){
					startBet = startBet.multiply(onLoss.getNumber());
					System.out.println(">> "+startBet);
				}else{
					//reset bet
					startBet = startingBet.getNumber();
					startBet = startBet.multiply(onWin.getNumber());
				}	
				
				System.out.println(betResponse.isSuccess()+" "+betResponse.getPayOut()+" "+betResponse.getStartingBalance());
				
				
			}
		}
		
		@Override
		protected void updateValue(String value) {
			// TODO Auto-generated method stub
			super.updateValue(value);	
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ApplicationSingleton.getInstance().getHomeController().infoLB.setText((value == null)? "" : value);
				}
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
						BigDecimal profit = (betResponse.getPayOut().compareTo(BigDecimal.ZERO) == 0)? startBet.negate() : betResponse.getPayOut().subtract(startBet);
						sessionProfit += profit.doubleValue();
						
						System.out.println(profit.doubleValue());
						BigDecimal balance = (betResponse.getPayOut().compareTo(BigDecimal.ZERO) == 0)? betResponse.getStartingBalance().subtract(startBet) : betResponse.getPayOut().subtract(startBet).add(betResponse.getStartingBalance());
						boolean win = (betResponse.getPayOut().compareTo(BigDecimal.ZERO) == 0)? false : true; 
						
						controller.topBalance.setText(balance.toString());
						controller.balanceLB.setText(balance.toString());
						
						Calendar date = Calendar.getInstance();
						date.setTimeInMillis(workDone);
						controller.tableBets.getItems().add(0, new Bet.BetBuilder(betResponse.getBetId(), 50, startBet.toPlainString(),win)
								.date(date.getTime().toString())
								.high(betType.getToggles().get(0).equals(betType.getSelectedToggle()))
								.roll(betResponse.getSecret())
								.profit(profit.toPlainString())
								.build());
						
						if(win){
							winsCount++;
							controller.winsLB.setText(winsCount+"");
						}else{
							lossesCount++;
							controller.lossesLB.setText(lossesCount+"");
						}
						
						controller.totalBetsLB.setText(++betCount+"");	
						
						System.out.println(">> "+betResponse.getPayOut().toPlainString());	
						controller.chartBets.getData().get(0).getData().add(new XYChart.Data<Number,Number>(++count, sessionProfit));
					}
				}
			});
			
		}
    	
    }
}
