package com.forecastera.service.financialmanagement.repository.capexrelatedexpense;
/*
 * @Author Kanishk Vats
 * @Create 05-07-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.capexrelatedexpense.CapexRelatedExpenseForecastExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CapexRelatedExpenseForecastExpenseRepo extends JpaRepository<CapexRelatedExpenseForecastExpense, Integer> {

    List<CapexRelatedExpenseForecastExpense> findAllByCapexForecastId(Integer key);
}
