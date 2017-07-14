package model.bet;

import java.math.BigDecimal;

public interface PlaceBetResponse {
	
	public boolean isSuccess();
	
	public boolean isChanceTooHigh();

	public boolean isChanceTooLow();

	public boolean isInsufficientFunds();

	public boolean isNoPossibleProfit();

	public boolean isMaxPayoutExceeded();
	
	public boolean isWinner();

	public long getBetId();

	public long getRollNumber();

	public BigDecimal getProfit();

	public BigDecimal getBalance();
	
	public PlaceBetRequest getRequest();
	
	public void setRequest(PlaceBetRequest request);
}
