package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 21-06-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.entity.FinanceMgmtFieldMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceMgmtFieldMapRepo extends JpaRepository<FinanceMgmtFieldMap, Integer> {

    @Query("select a from FinanceMgmtFieldMap a where a.settingType = 0")
    List<FinanceMgmtFieldMap> getAdminFinanceManagementComponents();

    @Query("select a from FinanceMgmtFieldMap a where a.settingType = 1")
    List<FinanceMgmtFieldMap> getAdminFinanceManagementButtons();

    @Query("select a from FinanceMgmtFieldMap a where a.settingType in (2,3) and a.tableType = :tableType")
    List<FinanceMgmtFieldMap> getAdminFinanceManagementTableFields(@Param("tableType") Integer tableType);
}
