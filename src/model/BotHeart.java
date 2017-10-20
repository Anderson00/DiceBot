package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import application.ApplicationSingleton;
import exceptions.ErrorsList;
import model.bet.PlaceBetRequest;
import model.bet.BeginSessionResponse;
import sites.client999dice.DiceWebAPI;
import sites.client999dice.PlaceBetResponse;
import sites.client999dice.SessionInfo;

public class BotHeart{
	
	public static final String API = "f3a5f0d8e5b24ba4a99ecd1a952f7064";
	private String site;
	private BeginSessionResponse session;
	private String user,pass;
	private BigDecimal balance = BigDecimal.ZERO;
	private List<ConsoleLog> logs;
	
	public BotHeart(String site){
		this.site = site;
		this.logs = new ArrayList<ConsoleLog>();
	}
	
	public BeginSessionResponse login(String user, String pass){
		this.user = user;
		this.pass = pass;
		switch(site.toLowerCase()){
			case "999dice":
				session = DiceWebAPI.BeginSession(BotHeart.API, user, pass);
				if(session.isSuccess()){
					this.balance = session.getSession().getBalance();
					return session;
				}
				return null;		
		}
		return null;
	}
	
	public BeginSessionResponse refreshSession(){		
		session = DiceWebAPI.BeginSession(BotHeart.API, user, pass);
		if(session.isSuccess()){
			this.balance = session.getSession().getBalance();
			return session;
		}
		else
			return null;
	}
	
	public BigDecimal getBalance(){
		return this.balance;
	}
		
	public PlaceBetResponse placeBet(boolean high,BigDecimal payIn,double chance){		
		PlaceBetRequest request = new sites.client999dice.PlaceBetRequest(payIn, high, chance);
		PlaceBetResponse betResponse = DiceWebAPI.PlaceBet((SessionInfo)this.session.getSession(), request);
		return betResponse;
	}
	
	public BeginSessionResponse getSession(){
		return this.session;
	}
	
	public void addLog(ConsoleLog log){
		ApplicationSingleton.getInstance().getHomeController().addLog(log);
	}
	
	public void updateLastLog(ConsoleLog log){
		ApplicationSingleton.getInstance().getHomeController().updateLastLog(log);
	}
	
	public static String Validate(BeginSessionResponse session){
		if(session != null){
			if(!session.isSuccess()){
				if(session.isInvalidApiKey())
					return ErrorsList.INVALID_APIKEY;
				if(session.isLoginRequired())
					return ErrorsList.LOGIN_REQUIRED;
				if(session.isWrongUsernameOrPassword())
					return ErrorsList.WRONG_USER_OR_PASS;
			}else
				return null;			
		}else 
			return ErrorsList.CONNECTION_ERROR;
		
		return null;
	}
	
	public static String Validate(PlaceBetResponse response){
		//if(!ses)
		return null;
	}	
	
	
	public static BigDecimal calculatePayout(boolean high, double chance){		
		return DiceWebAPI.CalculatePayoutMultiplier(high, chance);
	}
	
	public static BigDecimal calculatePayout(double payout){
		return DiceWebAPI.CalculateChancePayout(false, payout);
	}
}
