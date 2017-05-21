package application;

import controllers.HomeControllerView;
import controllers.ModeBasicControllerView;
import javafx.application.Application;
import model.BotHeart;
import sites.client999dice.BeginSessionResponse;

public class ApplicationSingleton {
	private static final ApplicationSingleton  uniqueInstance = new ApplicationSingleton();
	private Application application;
	private HomeControllerView homeController;
	private ModeBasicControllerView modeBasicController;// Mode of Bet BASIC
	private BotHeart botHeart;
	
	private ApplicationSingleton(){}
	
	//Setters
	public void setApplication(Application application){
		if(this.application == null)
			this.application = application;
	}
	
	public void setHomeController(HomeControllerView controller){
		if(this.homeController == null)
			this.homeController = controller;
	}
	
	public void setModeBasicController(ModeBasicControllerView controller){
		if(this.modeBasicController == null)
			this.modeBasicController = controller;
	}
	
	public void setBotHeart(BotHeart botHeart){
		this.botHeart = botHeart;
	}
	
	//Getters
	public HomeControllerView getHomeController(){
		return this.homeController;
	}
	
	public ModeBasicControllerView getModeBasicController(){
		return this.modeBasicController;
	}
	
	public BotHeart getBotHeart(){
		return this.botHeart;
	}
	
	//Singleton instance
	public static ApplicationSingleton getInstance(){
		return uniqueInstance;
	}
	
}