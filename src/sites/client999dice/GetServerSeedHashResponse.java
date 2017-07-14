package sites.client999dice;

import javax.json.JsonObject;

public final class GetServerSeedHashResponse extends DiceResponse {
	String serverSeedHash;

	@Override
	protected void setRawResponse(JsonObject resp) {
		super.setRawResponse(resp);

		if (resp.containsKey("Hash") && !resp.isNull("Hash")) {
			success = true;
			serverSeedHash = resp.getString("Hash");
		}
	}

	public String getServerSeedHash() {
		return serverSeedHash;
	}

}
