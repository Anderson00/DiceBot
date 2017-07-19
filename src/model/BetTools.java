package model;

import java.math.BigDecimal;
import java.util.Calendar;

import exceptions.ErrorsList;
import javafx.scene.chart.XYChart;
import model.bet.PlaceBetRequest;
import model.bet.PlaceBetResponse;
import model.bet.SessionInfo;

public class BetTools {
	
	public static String verifyBet(SessionInfo info,BigDecimal startBet){
		if(info == null)
			return ErrorsList.CONNECTION_ERROR;
		if(info.getBalance().compareTo(startBet) <= 0)
			return ErrorsList.INSUFFICIENT_FUNDS;
		return "unknown Error";
	}
	
	public static int fibonacci(int n)  {
		if(n == 0)
			return 0;
	    else if(n == 1)
	        return 1;
	    else
	        return fibonacci(n - 1) + fibonacci(n - 2);
	 }
}
