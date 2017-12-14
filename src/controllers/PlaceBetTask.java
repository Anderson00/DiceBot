package controllers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;
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
import model.dao.UserJpaDAO;
import model.entity.Bets;
import model.entity.User;
import sites.client999dice.DiceWebAPI;

public abstract class PlaceBetTask extends Task<String> {
	
	private PlaceBetResponse betResponse = null;
	private BigDecimalField startingBet = null, chance = null; 	
	private BigDecimal chanceToWin = BigDecimal.ZERO, balance = BigDecimal.ZERO;
	private long startBet = 0, btcStreakLoss = 0, btcStreakWin = 0;
	private HomeControllerView controller = ApplicationSingleton.getInstance().getHomeController();
	private BotHeart botHeart = ApplicationSingleton.getInstance().getBotHeart();
	private int wins, losses, numberOfBets, currentStreak, streakWin, streakLose, bigStreakWin, bigStreakLoss;
	private long winsCount,lossesCount,betCount;
	private static long count;
	private long sessionNumberOfBets = 0;
	private long sessionProfit;
	private BigDecimal profit2 = BigDecimal.ZERO;
	private static long profitSes;
	private boolean high = false, stopBetting = false, errorBetting = false;
	private ToggleGroup betType = null;
	private JFXButton stopBtn = null;
	private String mode = null;
	private boolean error = false;//Quando error for igual a true, PlaceBetTask para;
	private String finalMsg = null;	
	private long initialId = -1;// Bet Id initial
	
	public static enum ModesConsts{
		ONEBET, BASICMODE, PROGRAMMERMODE,MARTINGALE, LABOUCHÈRE, FIBONACCI, DALEMBERT, PRESETLIST, CUSTOM;
	}
	
	public PlaceBetTask(String mode, BigDecimalField startingBet, BigDecimalField chance, ToggleGroup betType, JFXButton stopBtn){
		this.mode = mode;
		this.startingBet = startingBet;
		this.betType = betType;
		this.stopBtn = stopBtn;
		startBet = toLongInteger(startingBet.getNumber());
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
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>> "+botHeart.getSession().getSession().getBalance().toPlainString());
		if(botHeart.getSession().getSession().getBalance().compareTo(new BigDecimal(startBet)) < 0){
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
			if(startBet == 0) return ErrorsList.INSUFFICIENT_FUNDS;
			
			//Execution Mode	
			high = betType.getToggles().get(0).equals(betType.getSelectedToggle());
			executionMode(this.mode, high);
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
					if(initialId == -1){
						initialId = betResponse.getBetId();
					}
					
					BigDecimal profit = betResponse.getProfit();
					sessionProfit += profit.intValue();
					profitSes += profit.doubleValue();
					
					profit2 = profit2.add(profit);
					
					System.out.println("Profit: "+betResponse.getProfit().toPlainString()+" "+profit2.toPlainString()+" "+new BigDecimal(sessionProfit).setScale(8, BigDecimal.ROUND_CEILING).toPlainString());
					
					boolean win = betResponse.isWinner(); 
					BigDecimal balanceAux = BotHeart.convertSatoshiToBTC(betResponse.getBalance());
					
					controller.topBalance.setText(balanceAux.toPlainString());
					controller.balanceLB.setText(balanceAux.toPlainString());
					
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis(workDone);//NadaZaver
					controller.tableBets.getItems().add(0, new Bet.BetBuilder(betResponse.getBetId(), chanceToWin.setScale(2, BigDecimal.ROUND_DOWN).toPlainString(), convertToCoin(startBet).setScale(8,RoundingMode.DOWN).toPlainString(),win)
							.date(date.getTime().toString())
							.high(high)
							.roll(betResponse.getRollNumber())
							.profit(BotHeart.convertSatoshiToBTC(profit).toPlainString())
							.build());
					
					//Persist Data hsqldb
					ApplicationSingleton app = ApplicationSingleton.getInstance();
					User user = app.getThisUser();
					UserJpaDAO userDao = UserJpaDAO.getInstance();
					user.setUserBets(new Bets.BetsBuilder(chanceToWin.setScale(2, BigDecimal.ROUND_DOWN).floatValue(), startBet, win)
										.betId(betResponse.getBetId())
										.initialId(initialId)
										.date(new Date(date.getTime().getTime()))
										.high(high)
										.roll((int)betResponse.getRollNumber())
										.profit(profit.longValue())
										.verified(true)//Falta definir thread para verificar se a aposta foi feita com sucesso
										.mode((short)returnModeConsts(mode).ordinal())
										.build());
					userDao.merge(user);
					
					
					boolean b = false;
					if(win){
						winsCount++;
						wins++;			
						streakLose = 0;
						streakWin++;						
						btcStreakWin += startBet;
						btcStreakLoss = 0;
						
						if(streakWin > bigStreakWin){
							bigStreakWin = streakWin;
						}
						controller.winsLB.setText(winsCount+"");
					}else{
						lossesCount++;
						losses++;
						streakWin = 0;
						streakLose++;
						btcStreakLoss += btcStreakLoss;
						btcStreakWin = 0;
						
						if(streakLose > bigStreakLoss){
							bigStreakLoss = streakLose;
						}
						controller.lossesLB.setText(lossesCount+"");
					}						
					controller.totalBetsLB.setText(++betCount+"");	

					BigDecimal prof = botHeart.getSession().getSession().getProfit();
					
					controller.profitLB.setText(BotHeart.convertSatoshiToBTC(prof).toPlainString());
					controller.wageredLB.setText(BotHeart.convertSatoshiToBTC(botHeart.getSession().getSession().getWagered()).toPlainString());
					
					controller.chartBets.getData().get(0).getData().add(new XYChart.Data<Number,Number>(++count, BotHeart.convertSatoshiToBTC(profitSes).doubleValue()));
					
					numberOfBets = (int) count;
					
					String msgLog = "Running(Count,Profit,Session profit,Streak): "+count+", "+
									convertToCoin(profitSes).toPlainString()+", "+
									convertToCoin(sessionProfit).toPlainString()+", "+
									"win:"+bigStreakWin+" losse:"+bigStreakLoss;
					botHeart.updateLastLog(new ConsoleLog(msgLog,false));
				}
			}
		});
	}
	
	public long toLongInteger(BigDecimal val){
		return val.multiply(new BigDecimal(100000000),
				MathContext.DECIMAL128).longValue();
	}
	
	public BigDecimal toBigDecimalLong(BigDecimal val){
		return val.multiply(new BigDecimal(100000000),
				MathContext.DECIMAL128);
	}
	
	public BigDecimal toBigDecimalLong(long val){
		return toBigDecimalLong(new BigDecimal(val));
	}
	
	public BigDecimal convertToCoin(BigDecimal val){
		return val.divide(new BigDecimal(100000000),
				MathContext.DECIMAL128).setScale(8, RoundingMode.DOWN);
	}
	
	public BigDecimal convertToCoin(long val){
		return convertToCoin(new BigDecimal(val));
	}
	
	public BigDecimal getBalance(){
		return this.balance;
	}
	
	public BigDecimal getSessionProfit() {
		return BotHeart.convertToCoin(sessionProfit);
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

	public long getStartBet() {
		return startBet;
	}
	
	public BigDecimal getValueBet(){//Valor ja convertido em long
		return toBigDecimalLong(this.startBet);
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

	public void setStartBet(long startBet) {
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
		setBtcStreakLoss(0);
		setBtcStreakWin(0);
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

	private void setBtcStreakLoss(long btcStreakLoss) {
		this.btcStreakLoss = btcStreakLoss;
	}

	private void setBtcStreakWin(long btcStreakWin) {
		this.btcStreakWin = btcStreakWin;
	}

	protected void setBigStreakLoss(int bigStreakLoss) {
		this.bigStreakLoss = bigStreakLoss;
	}

	public long getBtcStreakLoss() {
		return btcStreakLoss;
	}

	public long getBtcStreakWin() {
		return btcStreakWin;
	}
	
	public static void resetCount(){
		PlaceBetTask.count = 0;
	}

	
	private ModesConsts returnModeConsts(String mode){
		switch(mode.toLowerCase()){
		case "onebet":
			return ModesConsts.ONEBET;
		case "basicmode":
			return ModesConsts.BASICMODE;
		case "programmermode":
			return ModesConsts.PROGRAMMERMODE;		
		case "martingale":
			return ModesConsts.MARTINGALE;
		case "labouchère":
			return ModesConsts.LABOUCHÈRE;
		case "fibonacci":
			return ModesConsts.FIBONACCI;
		case "d´alembert":
			return ModesConsts.DALEMBERT;
		case "custom":
			return ModesConsts.CUSTOM;
		case "preset list":
			return ModesConsts.PRESETLIST;			
		default:
			return ModesConsts.ONEBET;
		}
	}
}
