package com.forecastera.service.financialmanagement.repository;

/*
 * @Author Uttam Kachhad
 * @Create 22-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.FinanceMgmtFieldMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceMgmtFieldMapRepo extends JpaRepository<FinanceMgmtFieldMap, Integer> {
    @Query("select fm from FinanceMgmtFieldMap as fm where fm.fields = :fieldNames and fm.tableType = :tableType")
    FinanceMgmtFieldMap getFinanceMgmtFieldIdByFieldName (@Param("fieldNames") String fieldNames, @Param("tableType") Integer tableType);
}
