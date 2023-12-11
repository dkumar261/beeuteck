package com.forecastera.service.financialmanagement.repository.otherdirectinhousecost;

import com.forecastera.service.financialmanagement.entity.otherdirectinhousecost.OtherDirectInHouseCostForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * @Author Uttam Kachhad
 * @Create 03-07-2023
 * @Description
 */
@Repository
public interface OtherDirectInHouseCostForecastRepo extends JpaRepository<OtherDirectInHouseCostForecast, Integer> {
}
