package sites.client999dice;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.json.JsonObject;

import model.bet.PlaceBetRequest;

public final class PlaceBetResponse extends DiceResponse implements model.bet.PlaceBetResponse{
	boolean chanceTooHigh, chanceTooLow, insufficientFunds, noPossibleProfit,
			maxPayoutExceeded, win;
	long betId, secret;
	BigDecimal payOut = BigDecimal.ZERO;
	BigDecimal profit = BigDecimal.ZERO;
	BigDecimal startingBalance;//balance antes do profit
	BigDecimal balance;//balance depos do profit
	
	PlaceBetRequest request = null;

	@Override
	protected void setRawResponse(JsonObject resp) {
		super.setRawResponse(resp);

		if (resp.containsKey("ChanceTooHigh"))
			chanceTooHigh = true;
		else if (resp.containsKey("ChanceTooLow"))
			chanceTooLow = true;
		else if (resp.containsKey("InsufficientFunds"))
			insufficientFunds = true;
		else if (resp.containsKey("NoPossibleProfit"))
			noPossibleProfit = true;
		else if (resp.containsKey("maxPayoutExceeded"))
			maxPayoutExceeded = true;
		else if (resp.containsKey("BetId") && resp.containsKey("PayOut")
				&& resp.containsKey("Secret")
				&& resp.containsKey("StartingBalance")) {
			
			success = true;			
			betId = resp.getJsonNumber("BetId").longValue();
			payOut = resp.getJsonNumber("PayOut").bigDecimalValue();
			secret = resp.getJsonNumber("Secret").longValue();
			startingBalance = resp.getJsonNumber("StartingBalance").bigDecimalValue();			
			BigDecimal amount = new BigDecimal(request.getAmount());
			System.out.println(">>>>>>>>>> "+ amount);
			
			profit = (payOut.compareTo(BigDecimal.ZERO) == 0)? amount.negate() : payOut.subtract(amount);
			win = (payOut.compareTo(BigDecimal.ZERO) == 0)? false : true;
			balance = startingBalance.add(profit);
		}
	}

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
	
	public boolean isWinner(){
		return this.win;
	}

	public long getBetId() {
		return betId;
	}
	
	public BigDecimal getProfit(){	
		return profit;
	}
	
	public long getRollNumber() {
		return secret;
	}

	public BigDecimal getBalance() {		
		return balance;
	}

	@Override
	public PlaceBetRequest getRequest() {
		return request;
	}

	@Override
	public void setRequest(PlaceBetRequest request) {
		this.request = request;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		return getRawResponse().toString();
	}
	
}
