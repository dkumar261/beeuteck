package com.forecastera.service.resourcemanagement.entity;

/*
 * @Author Sowjanya Aare
 * @Create 16-10-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Requested_Resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestedResources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "requested_role_id")
    private Integer requestedRoleId;

    @Column(name = "requested_resource_count")
    private Integer requestedResourceCount;

    @Column(name = "requested_resource_id")
    private Integer requestedResourceId;

    @Column(name = "requested_fte")
    private BigDecimal requestedFte;

    @Column(name = "allocation_start_date")
    private Date allocationStartDate;

    @Column(name = "allocation_end_date")
    private Date allocationEndDate;

    @Column(name = "request_status")
    private String requestStatus;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "notify")
    private String notify;

    @Column(name = "notify_pm")
    private String notifyPm;

    @Column(name = "pm_last_action")
    private String pmLastAction;

    @Column(name = "rm_last_action")
    private String rmLastAction;

    @Column(name = "viewed_by_pm_ids")
    private String viewedByPMIds;

    @Column(name = "viewed_by_rm_ids")
    private String viewedByRMIds;

}

