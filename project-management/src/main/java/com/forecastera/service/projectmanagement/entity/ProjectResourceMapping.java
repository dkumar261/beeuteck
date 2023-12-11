package com.forecastera.service.projectmanagement.entity;/*
 * @Author Ashutosh Mishra
 * @Create 6/12/2023
 * @Description
 */

import com.forecastera.service.projectmanagement.dto.request.PostKanbanData;
import com.forecastera.service.projectmanagement.dto.request.PostProjectResourceAllocationData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "project_resource_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResourceMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Integer mapId;

    @Column(name = "resource_id")
    private Integer resourceId;

    @Column(name="project_id")
    private Integer projectId;

    @Column(name="allocation_start_dt")
    private Date allocationStartDate;

    @Column(name="allocation_end_dt")
    private Date allocationEndDate;

    @Column(name="fte")
    private Double allocatedAvgFte;

    @Column(name="description")
    private String description;

    @Column(name = "is_deleted")
    private String isDeleted;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "request_id")
    private Integer requestId;

    @Column(name= "skill_id")
    private String skillId;

    @Column(name= "location_id")
    private String locationId;

    @Column(name= "emp_type_id")
    private String empTypeId;

    @Column(name= "emp_location_id")
    private String empLocationId;

    @Column(name= "requested_fte_avg")
    private Double requestedFteAvg;

//    public ProjectResourceMapping(PostKanbanData eachData) {
//        this.mapId = eachData.getMapId();
//        this.resourceId = eachData.getResourceId();
//        this.projectId = eachData.getProjectId();
//        this.allocationStartDate = eachData.getAllocationStartDate();
//        this.allocationEndDate = eachData.getAllocationEndDate();
//        this.allocatedAvgFte = eachData.getFte();
//        this.description= eachData.getDescription();
//        this.isDeleted= eachData.getIsDeleted();
//        this.roleId = eachData.getRoleId();
//    }

    public ProjectResourceMapping(PostProjectResourceAllocationData postProjectResourceAllocationData){
        this.mapId = postProjectResourceAllocationData.getMapId();
        this.resourceId = postProjectResourceAllocationData.getResourceId();
        this.projectId = postProjectResourceAllocationData.getProjectId();
        this.allocationStartDate = postProjectResourceAllocationData.getAllocationStartDate();
        this.allocationEndDate = postProjectResourceAllocationData.getAllocationEndDate();
        this.description = postProjectResourceAllocationData.getDescription();
        this.isDeleted = "0";
        this.roleId = postProjectResourceAllocationData.getRoleId();
        this.createdBy = postProjectResourceAllocationData.getCreatedBy();
        this.createdDate = postProjectResourceAllocationData.getCreatedDate();
        this.modifiedBy = postProjectResourceAllocationData.getModifiedBy();
        this.modifiedDate = postProjectResourceAllocationData.getModifiedDate();
        this.requestId = postProjectResourceAllocationData.getRequestId();
        this.skillId = postProjectResourceAllocationData.getSkillId();
        this.empTypeId = postProjectResourceAllocationData.getEmploymentTypeId();
        this.empLocationId = postProjectResourceAllocationData.getEmpLocationId();
        this.requestedFteAvg = 0d;
    }
}
