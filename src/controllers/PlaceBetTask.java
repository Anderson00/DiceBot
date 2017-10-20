package controllers;

import java.math.BigDecimal;
import java.math.MathContext;
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
	private BigDecimal startBet = BigDecimal.ZERO, chanceToWin = BigDecimal.ZERO, balance = BigDecimal.ZERO;
	private HomeControllerView controller = ApplicationSingleton.getInstance().getHomeController();
	private BotHeart botHeart = ApplicationSingleton.getInstance().getBotHeart();
	private int wins, losses, numberOfBets, currentStreak, streakWin, streakLose, bigStreakWin, bigStreakLoss;
	private long winsCount,lossesCount,betCount;
	private static long count;
	private long sessionNumberOfBets = 0;
	private static double sessionProfit;
	private BigDecimal profit2 = BigDecimal.ZERO, btcStreakLoss = BigDecimal.ZERO, btcStreakWin = BigDecimal.ZERO;
	private double profitSes;
	private boolean stopBetting = false, errorBetting = false;
	private ToggleGroup betType = null;
	private JFXButton stopBtn = null;
	private String mode = null;
	private boolean error = false;//Quando error for igual a true, PlaceBetTask para;
	private String finalMsg = null;	
	
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
			this.error = true;
			return ErrorsList.CONNECTION_ERROR;
		}
		
		//Check balance
		if(botHeart.getSession().getSession().getBalance().compareTo(startBet) <= 0){
			this.error = true;
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
		balance = botHeart.getBalance();
		
		updateMessage("Started");
		SessionInfo info = session.getSession();
		
		String msgLog = "Running(count,profit,streak): "+0+", "+
				new BigDecimal(0).setScale(8, BigDecimal.ROUND_CEILING).toPlainString()+", "+
				"win:"+0+" losse:"+0;
		updateMessage(msgLog);
		
		while(true){				
			if(stopBetting){ 
				if(finalMsg != null && finalMsg != "" ){
					return finalMsg;
				}
				return "Finished";				
			}
			if(startBet.compareTo(BigDecimal.ZERO) == 0) return ErrorsList.INSUFFICIENT_FUNDS;
			
			//Execution Mode	
			executionMode(this.mode, betType.getToggles().get(0).equals(betType.getSelectedToggle()));
			if(this.errorBetting){
				this.error = true;
				return "";
			}
			Thread.sleep(200);
		}
	}
	
	@Override
	protected void updateValue(String value) {
		// TODO Auto-generated method stub
		super.updateValue(value);	
		if(value == null || value.isEmpty())
			return;
		Platform.runLater(() ->{	
			if(stopBtn != null)
				stopBtn.fire();
			//ApplicationSingleton.getInstance().getHomeController().infoLB.setText();
			System.out.println(error);
			this.botHeart.addLog(new ConsoleLog((value == null)? "" : value,this.error));
		});			
	}
	
	@Override
	protected void updateMessage(String message) {
		// TODO Auto-generated method stub
		super.updateMessage(message);
		Platform.runLater(() ->{
			//ApplicationSingleton.getInstance().getHomeController().infoLB.setText((message == null)? "" : message);				
			this.botHeart.addLog(new ConsoleLog((message == null)? "" : message,this.error));
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
					sessionNumberOfBets++;
					
					BigDecimal profit = betResponse.getProfit();
					sessionProfit += profit.doubleValue();
					profitSes += profit.doubleValue();
					
					profit2 = profit2.add(profit);
					
					System.out.println("Profit: "+betResponse.getProfit().toPlainString()+" "+profit2.toPlainString()+" "+new BigDecimal(sessionProfit).setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					
					balance = betResponse.getBalance().setScale(8, BigDecimal.ROUND_CEILING);
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
						btcStreakWin = btcStreakWin.add(startBet,MathContext.DECIMAL128);
						btcStreakLoss = BigDecimal.ZERO;
						
						if(streakWin > bigStreakWin){
							bigStreakWin = streakWin;
						}
						controller.winsLB.setText(winsCount+"");
					}else{
						lossesCount++;
						losses++;
						streakWin = 0;
						streakLose++;
						btcStreakLoss = btcStreakLoss.add(startBet,MathContext.DECIMAL128);
						btcStreakWin = BigDecimal.ZERO;
						
						if(streakLose > bigStreakLoss){
							bigStreakLoss = streakLose;
						}
						controller.lossesLB.setText(lossesCount+"");
					}						
					controller.totalBetsLB.setText(++betCount+"");	

					BigDecimal prof = botHeart.getSession().getSession().getProfit();
					
					controller.profitLB.setText(prof.setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					controller.wageredLB.setText(botHeart.getSession().getSession().getWagered().toPlainString());
					
					controller.chartBets.getData().get(0).getData().add(new XYChart.Data<Number,Number>(++count, sessionProfit));
					
					numberOfBets = (int) count;
					
					String msgLog = "Running(count,profit,streak): "+count+", "+
									profit2.setScale(8, BigDecimal.ROUND_CEILING).toPlainString()+", "+
									"win:"+bigStreakWin+" losse:"+bigStreakLoss;
					botHeart.updateLastLog(new ConsoleLog(msgLog,false));
				}
			}
		});
		
	}
	
	public BigDecimal getBalance(){
		return this.balance;
	}
	
	public BigDecimal getSessionProfit() {
		return profit2;
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
	
	
	public void setFinalMessage(String msg){
		this.finalMsg = msg;
	}
	
	public long getSessionNumberOfBets(){
		return this.sessionNumberOfBets;
	}
	
	public void resetStreak(){
		setStreakLose(0);
		setStreakWin(0);
		setBigStreakLoss(0);
		setBigStreakWin(0);
		setBtcStreakLoss(BigDecimal.ZERO);
		setBtcStreakWin(BigDecimal.ZERO);
	}

	public int getBigStreakWin() {
		return bigStreakWin;
	}

	protected void setBigStreakWin(int bigStreakWin) {
		this.bigStreakWin = bigStreakWin;
	}

	public int getBigStreakLoss() {
		return bigStreakLoss;
	}

	private void setBtcStreakLoss(BigDecimal btcStreakLoss) {
		this.btcStreakLoss = btcStreakLoss;
	}

	private void setBtcStreakWin(BigDecimal btcStreakWin) {
		this.btcStreakWin = btcStreakWin;
	}

	protected void setBigStreakLoss(int bigStreakLoss) {
		this.bigStreakLoss = bigStreakLoss;
	}

	public BigDecimal getBtcStreakLoss() {
		return btcStreakLoss;
	}

	public BigDecimal getBtcStreakWin() {
		return btcStreakWin;
	}

}
