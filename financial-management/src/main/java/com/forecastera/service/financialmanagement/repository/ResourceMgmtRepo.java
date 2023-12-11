package com.forecastera.service.financialmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.ResourceMgmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceMgmtRepo extends JpaRepository<ResourceMgmt, Integer> {
}
