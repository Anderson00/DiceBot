package sites.client999dice;

import java.math.BigDecimal;

public class PlaceBetRequest implements model.bet.PlaceBetRequest{
	
	private BigDecimal payIn;
	private long amount,payout,guessLow,guessHigh;
	private boolean isHigh;
	private double chance,chanceAux;
	
	public PlaceBetRequest(BigDecimal payIn, boolean high, double chance){
		this.payIn = payIn;
		this.amount = Math.abs(DiceWebAPI.toSatoshis(payIn));
		this.isHigh = (guessLow == 0)? false : true;
		this.chance = chance;
		this.chanceAux = (999999.0) * (chance / 100.0);		
		
		guessLow = (high ? 999999 - (int)chanceAux : 0);
		guessHigh = (high ? 999999 : (int)chanceAux);
		
	}

	public void setPayIn(BigDecimal payIn) {
		this.payIn = payIn;
	}

	public BigDecimal getPayIn() {
		return payIn;
	}

	public long getGuessLow() {
		return guessLow;
	}

	public long getGuessHigh() {
		return guessHigh;
	}

	@Override
	public boolean isHigh() {
		// TODO Auto-generated method stub
		return isHigh;
	}

	@Override
	public long getAmount() {
		// TODO Auto-generated method stub
		return amount;
	}

	@Override
	public long getPayout() {
		// TODO Auto-generated method stub
		return payout;
	}

	@Override
	public void setAmount(long amount) {
		// TODO Auto-generated method stub
		this.amount = amount;
	}

	@Override
	public void setPayout(long payout) {
		// TODO Auto-generated method stub
		this.payout = payout;
	}

	@Override
	public double getChance() {
		// TODO Auto-generated method stub
		return chance;
	}

}
