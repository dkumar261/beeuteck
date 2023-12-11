package com.forecastera.service.financialmanagement.repository.directemployeedetails;

/*
 * @Author Uttam Kachhad
 * @Create 28-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.directemployeedetails.DirectEmployeeDetailsForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectEmployeeDetailsForecastRepo extends JpaRepository<DirectEmployeeDetailsForecast, Integer> {
}
