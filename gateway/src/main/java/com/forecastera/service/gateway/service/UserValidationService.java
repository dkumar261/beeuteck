package com.forecastera.service.gateway.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.forecastera.service.gateway.entity.UserLoginHistory;
import com.forecastera.service.gateway.model.ErrorResponse;
import com.forecastera.service.gateway.repository.UserValidationRepo;

@Component
public class UserValidationService {

	@Autowired
	private UserValidationRepo userLoginHistoryRepo;

	public ErrorResponse accessTokenValidation(String username, String accessToken) {
		ErrorResponse error = new ErrorResponse();
		error.setRequestAt(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		if (username == null) {
			error.setMessage("username header is missing");
			return error;
		}
		if (accessToken == null) {
			error.setMessage("accessToken header is missing");
			return error;
		}

		List<UserLoginHistory> getUserLoginHistory = userLoginHistoryRepo
				.findLoginDetailsByUsernameAndAccessToken(username, accessToken);
		
		if (getUserLoginHistory == null || getUserLoginHistory.isEmpty()) {
			error.setMessage("Invalid username or access token");
			return error;
		} else if (getUserLoginHistory.get(0).getLogoutTime() != null) {
			error.setMessage("User already logged out");
			return error;
		} else {
			return null;
		}
	}
}
