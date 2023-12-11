package com.forecastera.service.financialmanagement.repository.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 04-07-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.vendoremployeedetails.FinanceMgmtFieldValueForecastVendorEmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinanceMgmtFieldValueForecastVendorEmpRepo extends JpaRepository<FinanceMgmtFieldValueForecastVendorEmp, Integer> {

    Optional<FinanceMgmtFieldValueForecastVendorEmp> findByVendorForecastIdAndFieldId(Integer vendorForecastId, Integer fieldId);
}
