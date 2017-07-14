package model.bet;

import javax.json.JsonObject;

public abstract class DiceResponse {
	protected boolean success;
	protected int webStatusCode;
	protected String errorMessage;
	private JsonObject rawResponse;
	protected boolean rateLimited;
	
	public DiceResponse() {
		// TODO Auto-generated constructor stub
	}
	
	protected abstract void setRaw(JsonObject resp);
	
	public boolean isSuccess() {
		return success;
	}
	public int getWebStatusCode() {
		return webStatusCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public JsonObject getRawResponse() {
		return rawResponse;
	}
	public boolean isRateLimited() {
		return rateLimited;
	}
}
