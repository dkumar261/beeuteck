package com.forecastera.service.financialmanagement.repository.otherdirectinhousecost;

import com.forecastera.service.financialmanagement.entity.otherdirectinhousecost.OtherDirectInHouseCostForecastExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 04-07-2023
 * @Description
 */
@Repository
public interface OtherDirectInHouseForecastExpenseRepo extends JpaRepository<OtherDirectInHouseCostForecastExpense, Integer> {

    List<OtherDirectInHouseCostForecastExpense> findAllByOtherDirectInHouseCostForecastId(Integer key);
}
