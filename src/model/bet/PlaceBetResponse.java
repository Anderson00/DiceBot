package model.bet;

import java.math.BigDecimal;

public abstract class PlaceBetResponse extends DiceResponse {
	protected boolean chanceTooHigh, chanceTooLow, insufficientFunds, noPossibleProfit,
			maxPayoutExceeded;
	protected long betId, secret;
	protected BigDecimal payOut = BigDecimal.ZERO;
	protected BigDecimal startingBalance;	
	
	public boolean isChanceTooHigh() {
		return chanceTooHigh;
	}

	public boolean isChanceTooLow() {
		return chanceTooLow;
	}

	public boolean isInsufficientFunds() {
		return insufficientFunds;
	}

	public boolean isNoPossibleProfit() {
		return noPossibleProfit;
	}

	public boolean isMaxPayoutExceeded() {
		return maxPayoutExceeded;
	}

	public long getBetId() {
		return betId;
	}

	public long getSecret() {
		return secret;
	}

	public BigDecimal getPayOut() {
		return payOut;
	}

	public BigDecimal getBalance() {
		return startingBalance;
	}
	
}
