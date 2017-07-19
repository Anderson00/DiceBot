package controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.jfoenix.controls.JFXButton;

import application.ApplicationSingleton;
import controllers.ModeBasicControllerView.PlaceBetTask;
import exceptions.ErrorsList;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import jfxtras.labs.scene.control.BigDecimalField;
import model.Bet;
import model.BotHeart;
import model.bet.BeginSessionResponse;
import model.bet.PlaceBetResponse;
import model.bet.SessionInfo;

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
	private int count = 0;
    private double sessionProfit;
    private long winsCount = 0, lossesCount = 0, betCount = 0;
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
    private ToggleGroup onWin;
    
    @FXML
    private ListView<String> fibonacciList;
    
    @FXML
    private ToggleGroup levelReached;
    
    public static final int MAX_LEVEL = 100; // max level of fibonacci
    
    ArrayList<String> fibList = new ArrayList<>();
    //***
    
    @FXML
    void initialize() {
    	SessionInfo info = ApplicationSingleton.getInstance().getBotHeart().getSession().getSession();
    	winsCount = info.getBetWinCount();
    	betCount = info.getBetCount();
    	lossesCount = betCount - winsCount;
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
    	
    	// load fibList
    	loadFibList();
    	
    	startingBet.numberProperty().addListener((observable, oldValue, newValue) -> {
    		fibonacciList.getItems().clear();
    		System.out.println(newValue);
    		if(newValue.compareTo(BigDecimal.ZERO) <= 0)
    			return;
    		for(int i = 1; i <= MAX_LEVEL; i++){
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
	    	Thread task = new Thread(new PlaceBetTask(tabSelected().getText()));
	    	task.start();    		
    	});
    	
    	stopBtn.setOnAction(event -> {
    		stopBetting = true;
    		startBtn.setDisable(false);
    	});
    }
    
    public void loadFibList(){
    	fibList.add("0");
    	fibList.add("1");
		fibList.add("1");
		fibList.add("2");
		fibList.add("3");
		fibList.add("5");
		fibList.add("8");
		fibList.add("13");
		fibList.add("21");
		fibList.add("34");
		fibList.add("55");
		fibList.add("89");
		fibList.add("144");
		fibList.add("233");
		fibList.add("377");
		fibList.add("610");
		fibList.add("987");
		fibList.add("1597");
		fibList.add("2584");
		fibList.add("4181");
		fibList.add("6765");
		fibList.add("10946");
		fibList.add("17711");
		fibList.add("28657");
		fibList.add("46368");
		fibList.add("75025");
		fibList.add("121393");
		fibList.add("196418");
		fibList.add("317811");
		fibList.add("514229");
		fibList.add("832040");
		fibList.add("1346269");
		fibList.add("2178309");
		fibList.add("3524578");
		fibList.add("5702887");
		fibList.add("9227465");
		fibList.add("14930352");
		fibList.add("24157817");
		fibList.add("39088169");
		fibList.add("63245986");
		fibList.add("102334155");
		fibList.add("165580141");
		fibList.add("267914296");
		fibList.add("433494437");
		fibList.add("701408733");
		fibList.add("1134903170");
		fibList.add("1836311903");
		fibList.add("2971215073");
		fibList.add("4807526976");
		fibList.add("7778742049");
		fibList.add("12586269025");
		fibList.add("20365011074");
		fibList.add("32951280099");
		fibList.add("53316291173");
		fibList.add("86267571272");
		fibList.add("139583862445");
		fibList.add("225851433717");
		fibList.add("365435296162");
		fibList.add("591286729879");
		fibList.add("956722026041");
		fibList.add("1548008755920");
		fibList.add("2504730781961");
		fibList.add("4052739537881");
		fibList.add("6557470319842");
		fibList.add("10610209857723");
		fibList.add("17167680177565");
		fibList.add("27777890035288");
		fibList.add("44945570212853");
		fibList.add("72723460248141");
		fibList.add("117669030460994");
		fibList.add("190392490709135");
		fibList.add("308061521170129");
		fibList.add("498454011879264");
		fibList.add("806515533049393");
		fibList.add("1304969544928657");
		fibList.add("2111485077978050");
		fibList.add("3416454622906707");
		fibList.add("5527939700884757");
		fibList.add("8944394323791464");
		fibList.add("14472334024676221");
		fibList.add("23416728348467685");
		fibList.add("37889062373143906");
		fibList.add("61305790721611591");
		fibList.add("99194853094755497");
		fibList.add("160500643816367088");
		fibList.add("259695496911122585");
		fibList.add("420196140727489673");
		fibList.add("679891637638612258");
		fibList.add("1100087778366101931");
		fibList.add("1779979416004714189");
		fibList.add("2880067194370816120");
		fibList.add("4660046610375530309");
		fibList.add("7540113804746346429");
		fibList.add("12200160415121876738");
		fibList.add("19740274219868223167");
		fibList.add("31940434634990099905");
		fibList.add("51680708854858323072");
		fibList.add("83621143489848422977");
		fibList.add("135301852344706746049");
		fibList.add("218922995834555169026");
		fibList.add("354224848179261915075");
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
    
    public class PlaceBetTask extends Task<String>{
    	
    	PlaceBetResponse betResponse = null;
    	BigDecimal startBet = startingBet.getNumber().setScale(8, BigDecimal.ROUND_CEILING);    	
    	//BigDecimal startBet = startingBet.getNumber();
    	HomeControllerView controller = ApplicationSingleton.getInstance().getHomeController();
    	boolean executed = true;
    	int wins,losses,numberOfBets,currentStreak,streakWin,streakLose;
    	double profitSes;
    	String mode = null;
    	
    	public PlaceBetTask(String mode){
    		this.mode = mode;
    	}

		@Override
		protected String call() throws Exception {

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
			
			updateMessage("Start "+Calendar.getInstance().getTime());
			SessionInfo info = session.getSession();
			
			while(true){
				if(stopBetting){ 
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis(System.currentTimeMillis());
					return "Finished "+date.getTime();				
				}
				if(startBet.compareTo(BigDecimal.ZERO) == 0) return ErrorsList.INSUFFICIENT_FUNDS;
				executeBetMode(mode, betType2.getToggles().get(0).equals(betType2.getSelectedToggle()));
				//betResponse = botHeart.placeBet(betType2.getToggles().get(0).equals(betType2.getSelectedToggle()), startBet, chanceToWin.getNumber().doubleValue());
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
						controller.tableBets.getItems().add(0, new Bet.BetBuilder(betResponse.getBetId(), chanceBet.getNumber().setScale(2, BigDecimal.ROUND_DOWN).toPlainString(), startBet.setScale(8, BigDecimal.ROUND_CEILING).toPlainString(),win)
								.date(date.getTime().toString())
								.high(betType2.getToggles().get(0).equals(betType2.getSelectedToggle()))
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
						
					}
				}
			});
			
		}
		
		protected void executeBetMode(String mode, boolean high){
			
			//betResponse = botHeart.placeBet(betType2.getToggles().get(0).equals(betType2.getSelectedToggle()), startBet, chanceToWin.getNumber().doubleValue());
			
			switch(mode.toLowerCase()){
			case "martingale":
				
				break;
			case "labouchère":
				
				break;
				
			case "fibonacci":
				
				break;
			
			case "d´alembert":
				System.out.println("d´alembert");
				break;
			
			case "preset list":
				
				break;
			}
		}
    }
}
