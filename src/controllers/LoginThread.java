package controllers;

import java.io.IOException;
import java.math.RoundingMode;

import application.ApplicationSingleton;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import model.BotHeart;
import model.bet.BeginSessionResponse;
import model.bet.SessionInfo;
import model.dao.UserJpaDAO;
import model.entity.User;

public class LoginThread extends Task<BeginSessionResponse> {
	
	private LoginViewController controller;
	private String user, pass;
	private BotHeart.Sites site;
	
	public LoginThread(LoginViewController controller, String site, String user, String pass){
		this.controller = controller;
		this.user = user;
		this.pass = pass;
		this.site = BotHeart.returnSiteEnum(site);
	}

	@Override
	protected BeginSessionResponse call() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(site);
		BotHeart bot = new BotHeart(site);
		BeginSessionResponse session = bot.login(this.user, this.pass);
		if(session != null){
			ApplicationSingleton.getInstance().setBotHeart(bot);
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
			ApplicationSingleton singleton = ApplicationSingleton.getInstance();
			HomeControllerView controller = singleton.getHomeController();
			SessionInfo session = value.getSession();
			String balance = BotHeart.convertSatoshiToBTC(session.getBalance()).toPlainString();
			long wins = session.getBetWinCount();
			long betCount = session.getBetCount();
		    String profit = BotHeart.convertSatoshiToBTC(session.getProfit()).toPlainString();
		    String wagered = BotHeart.convertSatoshiToBTC(session.getWagered()).toPlainString();
			controller.topBalance.setText(balance);
			controller.balanceLB.setText(balance);
			controller.winsLB.setText(wins+"");
			controller.lossesLB.setText(betCount - wins+"");
			controller.totalBetsLB.setText(betCount+"");
			controller.profitLB.setText(profit);
			controller.wageredLB.setText(wagered);
			
			UserJpaDAO userDao = UserJpaDAO.getInstance();
			System.out.println(userDao.findAll().size());
			User user = userDao.findUser(this.user, site);
			if(user == null){
				user = new User();
				user.setNome(this.user);
				user.setSite(this.site.ordinal());
				userDao.persist(user);
			}
			singleton.setThisUser(user);
			
		}else{
			controller.removeModal();
			controller.setAvisoMsg("incorrect username or password");
		}
	}

}
