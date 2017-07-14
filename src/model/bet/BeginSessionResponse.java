package model.bet;

import model.bet.SessionInfo;

public interface BeginSessionResponse{
	
	public SessionInfo getSession();
	
	public boolean isSuccess();

	public boolean isInvalidApiKey();

	public boolean isLoginRequired();

	public boolean isWrongUsernameOrPassword();
}
