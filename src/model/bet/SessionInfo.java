package model.bet;

import java.math.BigDecimal;

public interface SessionInfo {
	
	public String getSessionCookie();
	
	public String getAccountCookie();
	
	public long getAccountId();
	
	public long getBetCount();
	
	public long getBetWinCount();
	
	public long getBetLoseCount();
	
	public String getUsername();
	
	public BigDecimal getProfit();
	
	public BigDecimal getWagered();
	
	public BigDecimal getBalance();

	public void setBetCount(long betCount);
	
	public void setBetWinCount(long betCount);
	
	public void setUsername(String username);
}
