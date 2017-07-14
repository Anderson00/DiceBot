package sites.client999dice;

import javax.json.JsonObject;


public class DiceResponse extends model.bet.DiceResponse {
	boolean success;
	int webStatusCode;
	String errorMessage;
	private JsonObject rawResponse;
	boolean rateLimited;
	
	DiceResponse(){}
	
	protected void setRawResponse(JsonObject resp)
	{
		rawResponse = resp;
		if (resp.containsKey("error") && !resp.isNull("error"))
			errorMessage=resp.getString("error");
		if (resp.containsKey("TooFast"))
			rateLimited=true;
		if (resp.containsKey("success"))
			success=true;		
	}
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
