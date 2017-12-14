package model.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Bets {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;	
	
	//Usado para saber onde começa e termina a sequencia de aposta
	private long initialId;//Primeira aposta
	private long idBet;
	private Date date;
	private boolean high;
	private boolean won;
	private boolean virified;
	private float chance;
	private long amount;
	private int roll;
	private long profit;
	private short mode;
	
	public Bets(){
		
	}
	
	public Bets(BetsBuilder builder){
		this.initialId = builder.initialId;
		this.idBet = builder.idBet;
		this.date = builder.date;
		this.high = builder.high;
		this.won = builder.won;
		this.virified = builder.virified;
		this.chance = builder.chance;
		this.amount = builder.amount;
		this.roll = builder.roll;
		this.profit = builder.profit;
		this.mode = builder.mode;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getInitialId() {
		return initialId;
	}

	public void setInitialId(long initialId) {
		this.initialId = initialId;
	}

	public long getIdBet() {
		return idBet;
	}

	public void setIdBet(long idBet) {
		this.idBet = idBet;
	}

	public Date getdate() {
		return date;
	}

	public void setdate(Date date) {
		this.date = date;
	}

	public boolean isHigh() {
		return high;
	}

	public void setHigh(boolean high) {
		this.high = high;
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public boolean isVirified() {
		return virified;
	}

	public void setVirified(boolean virified) {
		this.virified = virified;
	}

	public float getChance() {
		return chance;
	}

	public void setChance(float chance) {
		this.chance = chance;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public int getRoll() {
		return roll;
	}

	public void setRoll(int roll) {
		this.roll = roll;
	}

	public long getProfit() {
		return profit;
	}

	public void setProfit(long profit) {
		this.profit = profit;
	}

	public short getMode() {
		return mode;
	}

	public void setMode(short mode) {
		this.mode = mode;
	}
	

	public static class BetsBuilder{		
		//Usado para saber onde começa e termina a sequencia de aposta
		private long initialId;//Primeira aposta
		
		private long idBet;
		private Date date;
		private boolean high;
		private boolean won;
		private boolean virified;
		private float chance;
		private long amount;
		private int roll;
		private long profit;
		private short mode;
		
		public BetsBuilder(float chance, long amount, boolean won){
			this.chance = chance;
			this.amount = amount;
			this.won = won;
		}
		
		public BetsBuilder initialId(long id){
			this.initialId = id;
			return this;
		}
		
		public BetsBuilder betId(long id){
			this.idBet = id;
			return this;
		}

		public BetsBuilder date(Date date){
			this.date = date;
			return this;
		}

		public BetsBuilder high(boolean high){
			this.high = high;
			return this;
		}

		public BetsBuilder won(boolean won){
			this.won = won;
			return this;
		}
		
		public BetsBuilder verified(boolean verified){
			this.virified = verified;
			return this;
		}
		
		public BetsBuilder chance(float chance){
			this.chance = chance;
			return this;
		}
		
		public BetsBuilder amount(long amount){
			this.amount = amount;
			return this;
		}
		
		public BetsBuilder roll(int roll){
			this.roll = roll;
			return this;
		}
		
		public BetsBuilder profit(long profit){
			this.profit = profit;
			return this;
		}
		
		public BetsBuilder mode(short mode){
			this.mode = mode;
			return this;
		}
		
		public Bets build(){
			return new Bets(this);
		}
	}
}
