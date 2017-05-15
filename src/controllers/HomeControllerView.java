package controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

import application.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Bet;

public class HomeControllerView {

    @FXML
    protected ResourceBundle resources;

    @FXML
    protected URL location;

    @FXML
    protected AreaChart<Number, Number> chartBets;

    @FXML
    protected TableView<Bet> tableBets;
    
    @FXML
    protected TableColumn<Bet,Integer> id_column,
    					  chance_column, amount_column,
    					  profit_column;
    @FXML
    protected TableColumn<Bet, Object> high_column;

    @FXML
    protected TableColumn<Bet, String> date_column, roll_column;
    
    @FXML
    protected Label topBalance;
    
    @FXML
    protected SplitPane split_horizontal;
    
    @FXML
    protected SplitPane split_vertical;
    
    @FXML
    protected BorderPane bet_area;
    
    @FXML
    protected MenuBar menu;
    
    @FXML
    protected Menu menuView;
    
    @FXML
    protected StackPane modes; 
    
    private Main application;

    @FXML
    void initialize() {
        assert chartBets != null : "fx:id=\"chartBets\" was not injected: check your FXML file 'Dicebot.fxml'.";
        assert tableBets != null : "fx:id=\"tableBets\" was not injected: check your FXML file 'Dicebot.fxml'.";
        assert topBalance != null : "fx:id=\"topBalance\" was not injected: check your FXML file 'Dicebot.fxml'.";
        
        chartBets.setLegendVisible(false);
        NumberAxis xAxis = (NumberAxis) chartBets.getXAxis();
		NumberAxis yAxis = (NumberAxis) chartBets.getYAxis();
		
		NumberFormat format = new DecimalFormat("#.#E0");
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {

            @Override
            public String toString(Number number) {
            	if(number.doubleValue() == 0){
            		return "0";
            	}
            	
            	if(number.doubleValue() % number.intValue() == 0){
            		return ""+number.intValue()+"btc";
            	}
                return format.format(number.doubleValue())+" btc";
            }

            @Override
            public Number fromString(String string) {
                try {
                    return format.parse(string);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0 ;
                }
            }

        });
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {

			@Override
			public String toString(Number object) {
				// TODO Auto-generated method stub
				if(object.intValue() % 20 == 0){
					
					return object.intValue()+"";
				}
				return "";
			}

			@Override
			public Number fromString(String string) {
				// TODO Auto-generated method stub
				return Integer.parseInt(string);
			}
		});  
        
        new MenuViewListener(this, menu);
        
        
        id_column.setCellValueFactory(new PropertyValueFactory<Bet,Integer>("id"));
        date_column.setCellValueFactory(new PropertyValueFactory<Bet,String>("date"));
        high_column.setCellValueFactory(new PropertyValueFactory<Bet,Object>("high"));
		chance_column.setCellValueFactory(new PropertyValueFactory<Bet,Integer>("chance"));
		amount_column.setCellValueFactory(new PropertyValueFactory<Bet,Integer>("amount"));
		roll_column.setCellValueFactory(new PropertyValueFactory<Bet,String>("roll"));
		profit_column.setCellValueFactory(new PropertyValueFactory<Bet,Integer>("profit"));
		
		
		tableBets.setRowFactory(new Callback<TableView<Bet>, TableRow<Bet>>() {

			@Override
			public TableRow<Bet> call(TableView<Bet> param) {
				// TODO Auto-generated method stub
				TableRow<Bet> row = new TableRow<Bet>();
				//row.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				row.itemProperty().addListener(new ChangeListener<Bet>() {

					@Override
					public void changed(ObservableValue<? extends Bet> observable, Bet oldValue, Bet newValue) {
						// TODO Auto-generated method stub
						if(newValue != null)
							if(newValue.getWon())
								row.setStyle("-fx-background-color:green");
							else
								row.setStyle("-fx-background-color:red");
					}
					
				});
				
				return row;
			}
			
			
			
		});
		
				
		ObservableList dataTable = FXCollections.observableArrayList();
		tableBets.setItems(dataTable);
		
        
        SplitPane.setResizableWithParent(split_horizontal.getItems().get(1), false);
        //chartBets.setAnimated(false);
        TestChart();

    }
    
    void TestChart(){
    	System.out.println(chartBets.getXAxis());
    	
    	
    	
    	XYChart.Series<Number,Number> data = new XYChart.Series<>();
    	chartBets.getData().add(data);
				
		Task<Void> task = new Task<Void>() {

			int i = 0;
			int c = 0;
			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				
				while(true){
					updateProgress(0, 0);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			@Override
			protected void updateProgress(double workDone, double max) {
				// TODO Auto-generated method stub
				super.updateProgress(workDone, max);
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int q = (int)(Math.random()*10+1);
						tableBets.getItems().add(0, new Bet.BetBuilder(i, 50, 100,true)
								.high(true)
								.won(q%2==0)
								.profit(1)
								.build());
						
						data.getData().add(new XYChart.Data<Number,Number>(i, Math.random()%10*i));
						i++;
					}
				});
				
			}
			@Override
			protected void updateMessage(String message) {
				// TODO Auto-generated method stub
				super.updateMessage(message);
				
			}
		};
		
		Thread thr = new Thread(task, "testChart");
		thr.start();
		//new Thread(task).start();    	
    }
    
}
