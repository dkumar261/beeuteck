package com.forecastera.service.financialmanagement.repository.otherdirectinhousecost;

import com.forecastera.service.financialmanagement.entity.otherdirectinhousecost.OtherDirectInHouseCostBudgetExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 03-07-2023
 * @Description
 */
@Repository
public interface OtherDirectInHouseBudgetExpenseRepo extends JpaRepository<OtherDirectInHouseCostBudgetExpense, Integer> {

    List<OtherDirectInHouseCostBudgetExpense> findAllByOtherDirectInHouseCostBudgetId(Integer key);
}
