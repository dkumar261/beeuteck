package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 10-11-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.response.GetDelegatedResourceHistory;
import com.forecastera.service.usersettingsmanagement.entity.DelegatedResourceHistory;
import com.forecastera.service.usersettingsmanagement.entity.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRegistrationRepo extends JpaRepository<UserRegistration, Integer> {


   @Query("select ur from UserRegistration as ur where ur.emailId=:emailId")
   List<UserRegistration> getUserRegistrationByEmailId(String emailId);
}

