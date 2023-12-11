package com.forecastera.service.financialmanagement.repository.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 27-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.vendoremployeedetails.FinanceMgmtFieldValueBudgetVendorEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface FinanceMgmtFieldValueBudgetVendorEmpRepo extends JpaRepository<FinanceMgmtFieldValueBudgetVendorEmp, Integer> {

    Optional<FinanceMgmtFieldValueBudgetVendorEmp> findByVendorBudgetIdAndFieldId(Integer vendorBudgetId, Integer fieldId);
}
