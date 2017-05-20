package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.BorderPane;

public class MenuViewListener{
	
	private HomeControllerView context;
	private MenuBar menuBar;
	private boolean graphicIsVisible = true;
	private boolean tableIsVisible = true;
	
	public MenuViewListener(HomeControllerView context, MenuBar menuBar){
		this.context = context;
		this.menuBar = menuBar;	
		
		for(Menu menuView : menuBar.getMenus()){
			switch(menuView.getText().toLowerCase()){
				case "file":
					for(MenuItem it : menuView.getItems()){
						switch(it.getText().toLowerCase()){
							case "close":
								it.setOnAction(event -> {System.exit(0);});
							break;
						}
							
					}
				break;
				case "view":
					for(MenuItem it : menuView.getItems()){
						switch(it.getText().toLowerCase()){
							case "graphic":
								it.setOnAction(event -> onMenuItemGraphic(event, it)); 					
							break;
									
							case "table":
								it.setOnAction(event -> onMenuItemTable(event, it)); 	
							break;
								
							case "bet area":
								it.setOnAction(event -> onMenuItemBetArea(event, it)); 	
							break;
						}
							
					}
				break;
				case "mode":
					for(MenuItem it : menuView.getItems()){
						switch(it.getText().toLowerCase()){
							case "basic":	
								((RadioMenuItem) it).setSelected(true);
								it.setOnAction(event -> onModeBasic(event, it));	
								onModeBasic(null, it);// call first time
							break;
							case "advanced":
								it.setOnAction(event -> onModeAdvanced(event, it));
							break;
							case "programmer":
								it.setOnAction(event -> onModeProgrammer(event, it));
							break;
						}
							
					}
				break;
			}
			
		}
		
	}
	
	private void onMenuItemGraphic(ActionEvent event, MenuItem it){
		CheckMenuItem checkItem = (CheckMenuItem) it;
		
		if(checkItem.isSelected()){
			this.graphicIsVisible = true;
			//context.split_vertical.getItems().add(0, context.chartBets);
			context.split_vertical.getItems().add(context.chartBets);
			
		}else{		
			this.graphicIsVisible = false;
			context.split_vertical.getItems().remove(context.chartBets);
			/*int n = context.split_horizontal.getItems().indexOf();
			System.out.println(n);
			if(n >= 0){
				context.split_vertical.getItems().remove(n);			
			}*/
		}
		
	
	}
	
	private void onMenuItemTable(ActionEvent event, MenuItem it){
		CheckMenuItem checkItem = (CheckMenuItem) it;
		
		if(checkItem.isSelected()){
			this.tableIsVisible = true;
			//context.split_vertical.getItems().add(1, context.tableBets);			
			context.split_vertical.getItems().add(context.tableBets);
			
		}else{		
			this.tableIsVisible = false;
			context.split_vertical.getItems().remove(context.tableBets);
		}
		
	}
	
	private double dividerBackup;
	private void onMenuItemBetArea(ActionEvent event, MenuItem it){
		CheckMenuItem checkItem = (CheckMenuItem) it;
		
		if(checkItem.isSelected()){
			context.split_horizontal.getItems().add(context.bet_area);
			context.split_horizontal.setDividerPosition(0, dividerBackup);
		}else{	
			dividerBackup = context.split_horizontal.getDividerPositions()[0];
			context.split_horizontal.getItems().remove(context.bet_area);
		}
		
	}
	
	boolean back = false;
	private void onModeBasic(ActionEvent event, MenuItem it){	
		RadioMenuItem item = (RadioMenuItem) it;	
		if(!back)
		try {
			Node root = FXMLLoader.load(getClass().getResource("../resources/layouts/BasicBetMenu.fxml"));
			context.modes.getChildren().add(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		back = true;
	}
	
	private void onModeAdvanced(ActionEvent event, MenuItem it){		
		try {
			Node root = FXMLLoader.load(getClass().getResource("../resources/layouts/AdvancedBetMenu.fxml"));
			context.modes.getChildren().remove(0);
			context.modes.getChildren().add(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		back = false;
	}
	
	private void onModeProgrammer(ActionEvent event, MenuItem it){
		back = false;
	}
	
	public boolean graphicIsVisible(){
		return this.graphicIsVisible;		
	}
	
	public boolean tableIsVisible(){
		return this.tableIsVisible;		
	}

}
