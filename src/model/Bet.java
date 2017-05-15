package model;

import com.jfoenix.controls.JFXCheckBox;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Bet {
	private SimpleIntegerProperty id;
	private SimpleStringProperty date;
	private SimpleObjectProperty<JFXCheckBox> high;
	private SimpleIntegerProperty chance;
	private SimpleIntegerProperty amount;
	private SimpleStringProperty roll;
	private SimpleIntegerProperty profit;
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
	
	
	public int getId() {
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


	public int getAmount() {
		return amount.get();
	}


	public String getRoll() {
		return roll.get();
	}


	public int getProfit() {
		return profit.get();
	}
	
	public boolean getWon(){
		return this.won;
	}


	public static class BetBuilder{
		private SimpleIntegerProperty id;//required
		private SimpleStringProperty date;
		private SimpleObjectProperty<JFXCheckBox> high;
		private SimpleIntegerProperty chance;//required
		private SimpleIntegerProperty amount;//required
		private SimpleStringProperty roll;
		private SimpleIntegerProperty profit;
		private boolean won = false;
		
		public BetBuilder(int id, int chance, int amount, boolean won){
			this.id = new SimpleIntegerProperty(id);
			this.date = new SimpleStringProperty("");
			this.high = new SimpleObjectProperty<JFXCheckBox>(new JFXCheckBox());
			this.chance = new SimpleIntegerProperty(chance);
			this.amount = new SimpleIntegerProperty(amount);
			this.roll = new SimpleStringProperty("");
			this.profit = new SimpleIntegerProperty(0);
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
		
		public BetBuilder amount(int amount){
			this.amount = new SimpleIntegerProperty(amount);
			return this;
		}
		
		public BetBuilder roll(int roll){
			this.roll = new SimpleStringProperty(roll+"");
			return this;
		}
		
		public BetBuilder profit(int profit){
			this.profit = new SimpleIntegerProperty(profit);
			return this;
		}
		
		public BetBuilder won(boolean won){
			this.won = won;
			return this;
		}
		
		public Bet build(){
			return new Bet(this);
		}
	}
}
