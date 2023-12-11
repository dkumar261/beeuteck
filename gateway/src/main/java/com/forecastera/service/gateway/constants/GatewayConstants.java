package com.forecastera.service.gateway.constants;

public interface GatewayConstants {

	public String ACCESS_TO_TOKEN = "accessToken";
	public String USER_NAME = "username";
	public String LOGIN_URL = "/user-setting/login";

	public static enum USER_STATUS {
		AUTHENTICATED("AUTHENTICATED"), AUTHORIZED("AUTHORIZED"), NOT_AUTHENTICATED("NOT_AUTHENTICATED"),
		NOT_AUTHORIZED("NOT_AUTHORIZED");

		private final String value;

		USER_STATUS(final String newValue) {
			this.value = newValue;
		}

		public String getValue() {
			return this.value;
		}
	}
}