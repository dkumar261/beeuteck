package com.forecastera.service.financialmanagement.repository;

/*
 * @Author Uttam Kachhad
 * @Create 22-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.ResourceMgmtRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceMgmtRolesRepo extends JpaRepository<ResourceMgmtRoles, Integer> {
}
