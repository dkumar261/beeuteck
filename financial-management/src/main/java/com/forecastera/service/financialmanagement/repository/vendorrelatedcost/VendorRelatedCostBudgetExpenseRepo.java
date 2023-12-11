package com.forecastera.service.financialmanagement.repository.vendorrelatedcost;

import com.forecastera.service.financialmanagement.entity.vendorrelatedcost.VendorRelatedCostBudgetExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 05-07-2023
 * @Description
 */
@Repository
public interface VendorRelatedCostBudgetExpenseRepo extends JpaRepository<VendorRelatedCostBudgetExpense, Integer> {

    List<VendorRelatedCostBudgetExpense> findAllByVendorRelatedCostBudgetId(Integer key);
}
