package model;

import com.jfoenix.controls.JFXCheckBox;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Bet {
	private SimpleLongProperty id;
	private SimpleStringProperty date;
	private SimpleObjectProperty<JFXCheckBox> high;
	private SimpleIntegerProperty chance;
	private SimpleStringProperty amount;
	private SimpleStringProperty roll;
	private SimpleStringProperty profit;
	private boolean won = false;
	
	public Bet(BetBuilder builder){
		this.id = builder.id;
		this.date = builder.date;
		this.high = builder.high;
		this.chance = builder.chance;
		this.amount = builder.amount;
		this.roll = builder.roll;
		this.profit = builder.profit;
		this.won = builder.won;
	}
	
	
	public long getId() {
		return id.get();
	}


	public String getDate() {
		return date.get();
	}


	public JFXCheckBox getHigh() {
		return high.get();
	}


	public int getChance() {
		return chance.get();
	}


	public String getAmount() {
		return amount.get();
	}


	public String getRoll() {
		return roll.get();
	}


	public String getProfit() {
		return profit.get();
	}
	
	public boolean getWon(){
		return this.won;
	}


	public static class BetBuilder{
		private SimpleLongProperty id;//required
		private SimpleStringProperty date;
		private SimpleObjectProperty<JFXCheckBox> high;
		private SimpleIntegerProperty chance;//required
		private SimpleStringProperty amount;//required
		private SimpleStringProperty roll;
		private SimpleStringProperty profit;
		private boolean won = false;
		
		public BetBuilder(long id, int chance, String amount, boolean won){
			this.id = new SimpleLongProperty(id);
			this.date = new SimpleStringProperty("");
			this.high = new SimpleObjectProperty<JFXCheckBox>(new JFXCheckBox());
			this.chance = new SimpleIntegerProperty(chance);
			this.amount = new SimpleStringProperty(amount);
			this.roll = new SimpleStringProperty("");
			this.profit = new SimpleStringProperty("0");
			this.won = won;
			
		}
		
		public BetBuilder date(String date){
			this.date = new SimpleStringProperty(date);
			return this;
		}
		
		public BetBuilder high(boolean checked){
			JFXCheckBox checkbox = new JFXCheckBox();
			checkbox.setSelected(checked);
			this.high = new SimpleObjectProperty<JFXCheckBox>(checkbox);
			return this;
		}
		
		public BetBuilder chance(int chance){
			this.chance = new SimpleIntegerProperty(chance);
			return this;
		}
		
		public BetBuilder amount(String amount){
			this.amount = new SimpleStringProperty(amount);
			return this;
		}
		
		public BetBuilder roll(long roll){
			this.roll = new SimpleStringProperty(roll+"");
			return this;
		}
		
		public BetBuilder profit(String profit){
			this.profit = new SimpleStringProperty(profit);
			return this;
		}
		
		public Bet build(){
			return new Bet(this);
		}
	}
}
