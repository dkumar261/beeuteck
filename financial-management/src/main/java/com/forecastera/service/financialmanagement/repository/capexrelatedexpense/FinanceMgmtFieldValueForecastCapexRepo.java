package com.forecastera.service.financialmanagement.repository.capexrelatedexpense;
/*
 * @Author Kanishk Vats
 * @Create 05-07-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.capexrelatedexpense.FinanceMgmtFieldValueForecastCapex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinanceMgmtFieldValueForecastCapexRepo extends JpaRepository<FinanceMgmtFieldValueForecastCapex, Integer> {

    Optional<FinanceMgmtFieldValueForecastCapex> findByCapexIdAndFieldId(Integer forecastId, Integer fieldId);
}
