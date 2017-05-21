package model;

import javafx.concurrent.Task;
import sites.client999dice.BeginSessionResponse;
import sites.client999dice.DiceWebAPI;
public class BotHeart{
	
	public static final String API = "f3a5f0d8e5b24ba4a99ecd1a952f7064";
	private String site;
	private BeginSessionResponse session;
	
	public BotHeart(String site){
		this.site = site;
	}
	
	public BeginSessionResponse login(String user, String pass){
		switch(site.toLowerCase()){
			case "999dice":
				System.out.println("entrou");
				session = DiceWebAPI.BeginSession(API, user, pass);					
			return (session.isSuccess())? session : null;		
		}
		return null;
	}
	
	public BeginSessionResponse refreshSession(){		
		session = DiceWebAPI.BeginSession(this.API, session.getSession().getAccountCookie());
		System.out.println("entrou");
		if(session.isSuccess())
			return session;
		else
			return null;
	}
	
	public BeginSessionResponse getSession(){
		return this.session;
	}
	
	
}
