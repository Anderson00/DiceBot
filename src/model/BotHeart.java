package model;

import exceptions.ErrorsList;
import sites.client999dice.BeginSessionResponse;
import sites.client999dice.DiceWebAPI;
import sites.client999dice.PlaceBetResponse;

public class BotHeart{
	
	public static final String API = "f3a5f0d8e5b24ba4a99ecd1a952f7064";
	private String site;
	private BeginSessionResponse session;
	private String user,pass;
	
	public BotHeart(String site){
		this.site = site;
	}
	
	public BeginSessionResponse login(String user, String pass){
		this.user = user;
		this.pass = pass;
		switch(site.toLowerCase()){
			case "999dice":
				System.out.println("entrou");
				session = DiceWebAPI.BeginSession(BotHeart.API, user, pass);
			return (session.isSuccess())? session : null;		
		}
		return null;
	}
	
	public BeginSessionResponse refreshSession(){		
		session = DiceWebAPI.BeginSession(BotHeart.API, user, pass);
		if(session.isSuccess())
			return session;
		else
			return null;
	}
	
	public BeginSessionResponse getSession(){
		return this.session;
	}
	
	public static String Validate(BeginSessionResponse session){
		if(session != null){
			if(!session.isSuccess()){
				if(session.isInvalidApiKey())
					return ErrorsList.INVALID_APIKEY;
				if(session.isLoginRequired())
					return ErrorsList.LOGIN_REQUIRED;
				if(session.isRateLimited())
					return ErrorsList.RATE_LIMITED;
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
	
	
}
