package com.forecastera.service.resourcemanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 20-06-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "allocation_start_dt")
    private Date allocationStartDate;

    @Column(name = "allocation_end_dt")
    private Date allocationEndDate;

    @Column(name = "fte")
    private BigDecimal allocatedFteAvg;

    @Column(name = "description")
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

}
