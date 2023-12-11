package com.forecastera.service.financialmanagement.repository.vendorrelatedcost;

import com.forecastera.service.financialmanagement.entity.vendorrelatedcost.VendorRelatedCostForecastExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 05-07-2023
 * @Description
 */
@Repository
public interface VendorRelatedCostForecastExpenseRepo extends JpaRepository<VendorRelatedCostForecastExpense, Integer> {

    List<VendorRelatedCostForecastExpense> findAllByVendorRelatedCostForecastId(Integer key);
}
