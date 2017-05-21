package controllers;

import java.io.IOException;
import java.math.BigDecimal;

import application.ApplicationSingleton;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import model.BotHeart;
import sites.client999dice.BeginSessionResponse;
import sites.client999dice.DiceWebAPI;
import sites.client999dice.SessionInfo;

public class LoginThread extends Task<BeginSessionResponse> {
	
	private LoginViewController controller;
	private String site, user, pass;
	
	public LoginThread(LoginViewController controller, String site, String user, String pass){
		this.controller = controller;
		this.user = user;
		this.pass = pass;
		this.site = site;
	}

	@Override
	protected BeginSessionResponse call() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(site);
		BotHeart bot = new BotHeart(site);
		BeginSessionResponse session = bot.login(this.user, this.pass);
		if(session != null){
			ApplicationSingleton.getInstance().setSession(session);
			return session;
		}else
			return null;
	}
	
	@Override
	protected void updateValue(BeginSessionResponse value) {
		// TODO Auto-generated method stub
		super.updateValue(value);
		if(value != null){
			try {
				this.controller.setView(FXMLLoader.load(getClass().getResource("../resources/layouts/Dicebot.fxml")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HomeControllerView controller = ApplicationSingleton.getInstance().getHomeController();
			SessionInfo session = value.getSession();
			String balance = session.getBalance().toString();
			long wins = session.getBetWinCount();
			long betCount = session.getBetCount();
			controller.topBalance.setText(balance);
			controller.balanceLB.setText(balance);
			controller.winsLB.setText(wins+"");
			controller.lossesLB.setText(betCount - wins+"");
			controller.totalBetsLB.setText(betCount+"");
			System.out.println(session.getBetPayIn()+"   "+session.getBetPayOut());
			controller.profitLB.setText(session.getBetPayOut().add(session.getBetPayIn())+"");
			controller.wageredLB.setText(session.getBetPayIn().abs()+"");
			
		}else{
			controller.removeModal();
			controller.setAvisoMsg("incorrect username or password");
		}
	}

}