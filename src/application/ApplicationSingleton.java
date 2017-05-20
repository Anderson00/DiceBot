package application;

import controllers.HomeControllerView;
import controllers.ModeBasicControllerView;
import javafx.application.Application;

public class ApplicationSingleton {
	private static final ApplicationSingleton  uniqueInstance = new ApplicationSingleton();
	private Application application;
	private HomeControllerView homeController;
	private ModeBasicControllerView modeBasicController;// Mode of Bet BASIC
	
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
	
	//Getters
	public HomeControllerView getHomeController(){
		return this.homeController;
	}
	
	public ModeBasicControllerView getModeBasicController(){
		return this.modeBasicController;
	}
	
	//Singleton instance
	public static ApplicationSingleton getInstance(){
		return uniqueInstance;
	}
	
}