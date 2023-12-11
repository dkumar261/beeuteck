package com.forecastera.service.financialmanagement.repository.vendorrelatedcost;

import com.forecastera.service.financialmanagement.entity.vendorrelatedcost.VendorRelatedCostFieldValueForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * @Author Uttam Kachhad
 * @Create 05-07-2023
 * @Description
 */
@Repository
public interface VendorRelatedCostFieldValueForecastRepo extends JpaRepository<VendorRelatedCostFieldValueForecast, Integer> {

    Optional<VendorRelatedCostFieldValueForecast> findByVendorRelatedCostForecastIdAndFieldId(Integer id, Integer fieldId);
}
