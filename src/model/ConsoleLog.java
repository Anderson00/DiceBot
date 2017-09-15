package model;

import java.util.Calendar;
import java.util.Date;

public class ConsoleLog {
	//private int id;
	private Date date;
	private String msg;
	private boolean error = false;
	
	public ConsoleLog(){
		date = Calendar.getInstance().getTime();
		msg = "";
	}
	
	public ConsoleLog(String msg, boolean error){
		this();
		this.msg = msg;
		this.error = error;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public boolean isError(){
		return this.error;
	}
	
	public void updateMsg(String msg){
		this.msg = msg;
		this.date = Calendar.getInstance().getTime();
	}
	
	public void setError(boolean error){
		this.error = error;
	}
}
