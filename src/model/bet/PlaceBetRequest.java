package model.bet;

public interface PlaceBetRequest {
	
	public boolean isHigh();
	
	public long getAmount();
	
	public long getPayout();
	
	public double getChance();
	
	public void setAmount(long amount);
	
	public void setPayout(long payout);
}
