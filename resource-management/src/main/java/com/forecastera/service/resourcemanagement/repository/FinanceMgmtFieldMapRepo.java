package com.forecastera.service.resourcemanagement.repository;

/*
 * @Author Kanishk Vats
 * @Create 21-07-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.entity.FinanceMgmtFieldMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceMgmtFieldMapRepo extends JpaRepository<FinanceMgmtFieldMap, Integer> {

    @Query("select fm from FinanceMgmtFieldMap as fm where fm.fields in :fieldNames and fm.tableType = :tableType")
    List<FinanceMgmtFieldMap> getFinanceMgmtFieldIdByFieldName (@Param("fieldNames") List<String> fieldNames, @Param("tableType") Integer tableType);
}
