package com.forecastera.service.financialmanagement.repository.capexrelatedexpense;
/*
 * @Author Kanishk Vats
 * @Create 05-07-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.capexrelatedexpense.CapexRelatedExpenseBudgetExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CapexRelatedExpenseBudgetExpenseRepo extends JpaRepository<CapexRelatedExpenseBudgetExpense, Integer> {

    List<CapexRelatedExpenseBudgetExpense> findAllByCapexBudgetId(Integer key);
}
