package controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;

import application.ApplicationSingleton;
import exceptions.ErrorsList;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleGroup;
import jfxtras.labs.scene.control.BigDecimalField;
import model.Bet;
import model.BotHeart;
import model.ConsoleLog;
import model.bet.BeginSessionResponse;
import model.bet.PlaceBetResponse;
import model.bet.SessionInfo;

public abstract class PlaceBetTask extends Task<String> {
	
	private PlaceBetResponse betResponse = null;
	private BigDecimalField startingBet = null, chance = null; 	
	private BigDecimal startBet = BigDecimal.ZERO, chanceToWin = BigDecimal.ZERO;
	private HomeControllerView controller = ApplicationSingleton.getInstance().getHomeController();
	private BotHeart botHeart = ApplicationSingleton.getInstance().getBotHeart();
	private int wins,losses,numberOfBets,currentStreak,streakWin,streakLose;
	private long winsCount,lossesCount,betCount;
	private static long count;
	private static double sessionProfit;
	private BigDecimal profit2 = BigDecimal.ZERO;
	private double profitSes;
	private boolean stopBetting = false, errorBetting = false;
	private ToggleGroup betType = null;
	private JFXButton stopBtn = null;
	private String mode = null;
	private boolean error = false;//Quando error for igual a true, PlaceBetTask para; 
	
	public PlaceBetTask(String mode, BigDecimalField startingBet, BigDecimalField chance, ToggleGroup betType, JFXButton stopBtn){
		this.mode = mode;
		this.startingBet = startingBet;
		this.betType = betType;
		this.stopBtn = stopBtn;
		startBet = startingBet.getNumber().setScale(8, RoundingMode.CEILING);
		chanceToWin = chance.getNumber();
	}
	
	@Override
	protected String call() throws Exception {
		// TODO Auto-generated method stub
		
		BeginSessionResponse session = ApplicationSingleton.getInstance().getBotHeart().refreshSession();		
		if(session == null){
			error = true;
			return ErrorsList.CONNECTION_ERROR;
		}
		
		//Check balance
		if(botHeart.getSession().getSession().getBalance().compareTo(startBet) <= 0){
			error = true;
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
		
		updateMessage("Started");
		SessionInfo info = session.getSession();
		
		String msgLog = "Running(count,profit,streak): "+0+", "+
				new BigDecimal(0).setScale(8, BigDecimal.ROUND_CEILING).toPlainString()+", "+
				"win:"+0+" losse:"+0;
		updateMessage(msgLog);
		
		while(true){				
			if(stopBetting){ 
				Calendar date = Calendar.getInstance();
				date.setTimeInMillis(System.currentTimeMillis());
				return "Finished";				
			}
			if(startBet.compareTo(BigDecimal.ZERO) == 0) return ErrorsList.INSUFFICIENT_FUNDS;
			
			//Execution Mode	
			executionMode(this.mode, betType.getToggles().get(0).equals(betType.getSelectedToggle()));
			if(this.errorBetting){
				error = true;
				return "";
			}
		}
	}
	
	@Override
	protected void updateValue(String value) {
		// TODO Auto-generated method stub
		super.updateValue(value);	
		if(value == null || value.isEmpty())
			return;
		Platform.runLater(() ->{				
			stopBtn.fire();
			//ApplicationSingleton.getInstance().getHomeController().infoLB.setText();
			this.botHeart.addLog(new ConsoleLog((value == null)? "" : value,error));
		});			
	}
	
	@Override
	protected void updateMessage(String message) {
		// TODO Auto-generated method stub
		super.updateMessage(message);
		Platform.runLater(() ->{
			//ApplicationSingleton.getInstance().getHomeController().infoLB.setText((message == null)? "" : message);				
			this.botHeart.addLog(new ConsoleLog((message == null)? "" : message,false));
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
				if(betResponse.isSuccess()){						
					BigDecimal profit = betResponse.getProfit();
					sessionProfit += profit.doubleValue();
					profitSes += profit.doubleValue();
					
					profit2 = profit2.add(profit);
					
					System.out.println("Profit: "+betResponse.getProfit().toPlainString()+" "+profit2.toPlainString()+" "+new BigDecimal(sessionProfit).setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					
					BigDecimal balance = betResponse.getBalance().setScale(8, BigDecimal.ROUND_CEILING);
					boolean win = betResponse.isWinner(); 
					
					controller.topBalance.setText(balance.toPlainString());
					controller.balanceLB.setText(balance.toPlainString());
					
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis(workDone);
					controller.tableBets.getItems().add(0, new Bet.BetBuilder(betResponse.getBetId(), chanceToWin.setScale(2, BigDecimal.ROUND_DOWN).toPlainString(), startBet.setScale(8, BigDecimal.ROUND_CEILING).toPlainString(),win)
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
					controller.totalBetsLB.setText(++betCount+"");	

					BigDecimal prof = botHeart.getSession().getSession().getProfit();
					
					controller.profitLB.setText(prof.setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					controller.wageredLB.setText(botHeart.getSession().getSession().getWagered().toPlainString());
					
					controller.chartBets.getData().get(0).getData().add(new XYChart.Data<Number,Number>(++count, sessionProfit));
					
					String msgLog = "Running(count,profit,streak): "+count+", "+
									profit2.setScale(8, BigDecimal.ROUND_CEILING).toPlainString()+", "+
									"win:"+streakWin+" losse:"+streakLose;
					botHeart.updateLastLog(new ConsoleLog(msgLog,false));
				}
			}
		});
		
	}
	
	public double getProfitSes() {
		return profitSes;
	}

	public void setProfitSes(double profitSes) {
		this.profitSes = profitSes;
	}

	public int getStreakWin() {
		return streakWin;
	}

	public void setStreakWin(int streakWin) {
		this.streakWin = streakWin;
	}

	public int getStreakLose() {
		return streakLose;
	}

	public void setStreakLose(int streakLose) {
		this.streakLose = streakLose;
	}

	public long getLossesCount() {
		return lossesCount;
	}

	public void setLossesCount(long lossesCount) {
		this.lossesCount = lossesCount;
	}

	public double getSessionProfit() {
		return sessionProfit;
	}

	public void setSessionProfit(double sessionProfit) {
		this.sessionProfit = sessionProfit;
	}

	public PlaceBetResponse getBetResponse() {
		return betResponse;
	}

	public void setBetResponse(PlaceBetResponse betResponse) {
		this.betResponse = betResponse;
	}
	
	public abstract void executionMode(String mode,boolean high) throws Exception;

	public boolean isStopBetting() {
		return stopBetting;
	}

	public void setStopBetting(boolean stopBetting) {
		this.stopBetting = stopBetting;
	}

	public BigDecimal getStartBet() {
		return startBet;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public void setStartBet(BigDecimal startBet) {
		this.startBet = startBet;
	}

	public BigDecimal getChanceToWin() {
		return chanceToWin;
	}

	public void setChanceToWin(BigDecimal chanceToWin) {
		this.chanceToWin = chanceToWin;
	}

	public int getNumberOfBets() {
		return numberOfBets;
	}

	public void incrementNumberOfBets() {
		this.numberOfBets++;
	}
	
	public BigDecimalField getStartingBet() {
		return startingBet;
	}

	public BigDecimalField getChance() {
		return chance;
	}
	
	public void setErrorBetting(boolean errorBetting) {
		this.errorBetting = errorBetting;
	}

}
