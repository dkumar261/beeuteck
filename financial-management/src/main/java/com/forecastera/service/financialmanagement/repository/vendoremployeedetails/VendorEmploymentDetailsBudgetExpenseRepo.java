package com.forecastera.service.financialmanagement.repository.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 27-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.vendoremployeedetails.VendorEmploymentDetailsBudgetExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VendorEmploymentDetailsBudgetExpenseRepo extends JpaRepository<VendorEmploymentDetailsBudgetExpense, Integer> {

    List<VendorEmploymentDetailsBudgetExpense> findAllByVendorDetailsBudgetId(Integer vendor_employment_details_budget_id);
}
