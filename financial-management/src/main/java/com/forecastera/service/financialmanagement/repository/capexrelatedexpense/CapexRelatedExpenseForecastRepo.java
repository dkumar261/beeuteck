package com.forecastera.service.financialmanagement.repository.capexrelatedexpense;
/*
 * @Author Kanishk Vats
 * @Create 05-07-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.capexrelatedexpense.CapexRelatedExpenseForecast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapexRelatedExpenseForecastRepo extends JpaRepository<CapexRelatedExpenseForecast, Integer> {
}
