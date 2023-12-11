package com.forecastera.service.financialmanagement.repository.directemployeedetails;

import com.forecastera.service.financialmanagement.entity.directemployeedetails.FinanceDirectEmployeeForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * @Author Uttam Kachhad
 * @Create 28-06-2023
 * @Description
 */
@Repository
public interface FinanceDirectEmployeeForecastRepo extends JpaRepository<FinanceDirectEmployeeForecast, Integer> {

    Optional<FinanceDirectEmployeeForecast> findByDirectEmployeeForecastIdAndFieldId(Integer directEmployeeForecastId, Integer fieldId);
}
