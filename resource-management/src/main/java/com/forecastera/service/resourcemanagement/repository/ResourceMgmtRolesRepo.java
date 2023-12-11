package com.forecastera.service.resourcemanagement.repository;

/*
 * @Author Kanishk Vats
 * @Create 12-06-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.entity.ResourceMgmtRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMgmtRolesRepo extends JpaRepository<ResourceMgmtRoles,Integer> {

    @Query(value = "select string_agg(rr.role,',') from role_master as rr where rr.role_id in :roleIds", nativeQuery = true)
    String getRolesByIds(@Param("roleIds") List<Integer> roleIds);
}
