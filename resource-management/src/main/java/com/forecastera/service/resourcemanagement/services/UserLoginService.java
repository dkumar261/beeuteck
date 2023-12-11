package com.forecastera.service.resourcemanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 06-09-2023
 * @Description
 */


import com.forecastera.service.resourcemanagement.commonResponseUtil.Error;
import com.forecastera.service.resourcemanagement.entity.UserLoginHistory;
import com.forecastera.service.resourcemanagement.repository.UserLoginHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserLoginService {

    @Autowired
    private UserLoginHistoryRepo userLoginHistoryRepo;

    public Object accessTokenValidation(String username, String accessToken){
        List<UserLoginHistory> getUserLoginHistory = userLoginHistoryRepo.findLoginDetailsByUsernameAndAccessToken(username, accessToken);
        if(getUserLoginHistory==null || getUserLoginHistory.isEmpty()){
            Error error = new Error<>();
            error.setRequestAt(new Date());
            error.setMessage("Invalid username or access token");
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }
        else if (getUserLoginHistory.get(0).getLogoutTime()!=null){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage("User already logged out");
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }
        else{
            return true;
        }
    }
}
