package com.forecastera.service.gateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.forecastera.service.gateway.entity.UserLoginHistory;

@Repository
public interface UserValidationRepo extends JpaRepository<UserLoginHistory, Integer> {

	List<UserLoginHistory> findLoginDetailsByUsernameAndAccessToken(@Param("username") String username,
			@Param("accessToken") String accessToken);
}
