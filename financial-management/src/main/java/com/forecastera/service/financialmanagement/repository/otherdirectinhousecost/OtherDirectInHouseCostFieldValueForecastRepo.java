package com.forecastera.service.financialmanagement.repository.otherdirectinhousecost;

import com.forecastera.service.financialmanagement.entity.otherdirectinhousecost.OtherDirectInHouseCostFieldValueForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * @Author Uttam Kachhad
 * @Create 03-07-2023
 * @Description
 */
@Repository
public interface OtherDirectInHouseCostFieldValueForecastRepo extends JpaRepository<OtherDirectInHouseCostFieldValueForecast, Integer> {

    Optional<OtherDirectInHouseCostFieldValueForecast> findByOtherDirectInHouseCostForecastIdAndFieldId(Integer id, Integer fieldId);
}
