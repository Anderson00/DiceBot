package sites.client999dice;

import javax.json.JsonObject;

public final class ChangePasswordResponse extends DiceResponse {
	boolean wrongPassword;

	@Override
	protected void setRawResponse(JsonObject resp) {
		super.setRawResponse(resp);

		if (resp.containsKey("WrongPassword"))
			wrongPassword = true;
	}

	public boolean isWrongPassword() {
		return wrongPassword;
	}

}
