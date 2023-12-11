package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 12-06-2023
 * @Description
 */


import com.forecastera.service.resourcemanagement.entity.ResourceMgmtEmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceMgmtEmploymentTypeRepo extends JpaRepository<ResourceMgmtEmploymentType,Integer> {

    @Query("select ret.employmentType from ResourceMgmtEmploymentType as ret where ret.empTypeId = :empTypeId")
    String getEmploymentTypeById(@Param("empTypeId") Integer empTypeId);
}
