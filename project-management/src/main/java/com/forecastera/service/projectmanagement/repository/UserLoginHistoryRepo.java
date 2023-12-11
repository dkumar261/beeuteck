package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 01-09-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginHistoryRepo extends JpaRepository<UserLoginHistory, Integer> {

    @Query("select ulh from UserLoginHistory ulh where ulh.username = :username and ulh.accessToken = :accessToken")
    List<UserLoginHistory> findLoginDetailsByUsernameAndAccessToken(@Param("username") String username, @Param("accessToken") String accessToken);
}
