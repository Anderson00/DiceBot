package sites.client999dice;

import java.math.BigDecimal;

public final class SessionInfo implements model.bet.SessionInfo{
	String sessionCookie, accountCookie, email, emergencyAddress,
			depositAddress, username;
	long accountId, clientSeed, betCount, betWinCount, betLoseCount;
	int maxBetBatchSize;
	BigDecimal betPayIn = BigDecimal.ZERO;
	BigDecimal betPayOut = BigDecimal.ZERO;
	BigDecimal balance = BigDecimal.ZERO;
	
	BigDecimal profit = BigDecimal.ZERO;
	BigDecimal wagered = BigDecimal.ZERO;

	SessionInfo(String _sessionCookie, long _accountId, int _maxBetBatchSize) {
		sessionCookie = _sessionCookie;
		accountId = _accountId;
		maxBetBatchSize = _maxBetBatchSize;
	}

	private static BigDecimal forceNotNull(BigDecimal num) {
		return num == null ? BigDecimal.ZERO : num;
	}

	public String getSessionCookie() {
		return sessionCookie;
	}

	public String getAccountCookie() {
		return accountCookie;
	}

	public String getEmail() {
		return email;
	}

	public String getEmergencyAddress() {
		return emergencyAddress;
	}

	public String getDepositAddress() {
		return depositAddress;
	}

	public String getUsername() {
		return username;
	}

	public long getAccountId() {
		return accountId;
	}

	public long getClientSeed() {
		return clientSeed;
	}

	public long getBetCount() {
		return betCount;
	}

	public long getBetWinCount() {
		return betWinCount;
	}

	public int getMaxBetBatchSize() {
		return maxBetBatchSize;
	}

	public BigDecimal getProfit() {
		return forceNotNull(betPayOut.add(betPayIn));
	}

	public BigDecimal getWagered() {
		return forceNotNull(betPayIn.abs());
	}

	public BigDecimal getBalance() {
		return forceNotNull(balance);
	}
	
	public void setUsername(String username){
		this.username = username;
	}

	@Override
	public long getBetLoseCount() {
		// TODO Auto-generated method stub
		return betLoseCount;
	}

	@Override
	public void setBetCount(long betCount) {
		// TODO Auto-generated method stub
		betLoseCount = betCount - betWinCount;
		this.betCount = betCount;
	}

	@Override
	public void setBetWinCount(long betCount) {
		// TODO Auto-generated method stub
		betLoseCount = betCount - betWinCount;
		this.betWinCount = betCount;
	}

}
