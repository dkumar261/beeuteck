package com.forecastera.service.projectmanagement.entity;

/*
 * @Author Sowjanya Aare
 * @Create 16-10-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.dto.request.PostProjectResourceAllocationData;
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

//    @Column(name = "requested_fte")
//    private BigDecimal requestedFte;

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
    private Boolean notify;

    @Column(name = "notify_pm")
    private Boolean notifyPm;

    @Column(name = "pm_last_action")
    private String pmLastAction;

    @Column(name = "rm_last_action")
    private String rmLastAction;

    @Column(name = "viewed_by_pm_ids")
    private String viewedByPMIds;

    @Column(name = "viewed_by_rm_ids")
    private String viewedByRMIds;

    public RequestedResources(PostProjectResourceAllocationData postProjectResourceAllocationData){
        this.requestId = postProjectResourceAllocationData.getRequestId();
        this.projectId = postProjectResourceAllocationData.getProjectId();
        if(postProjectResourceAllocationData.getRoleId()!=null && !postProjectResourceAllocationData.getRoleId().isEmpty()) {
            this.requestedRoleId = Integer.valueOf(postProjectResourceAllocationData.getRoleId());
        }
        this.requestedResourceCount = 1;
        this.requestedResourceId = postProjectResourceAllocationData.getResourceId();
        this.allocationStartDate = postProjectResourceAllocationData.getAllocationStartDate();
        this.allocationEndDate = postProjectResourceAllocationData.getAllocationEndDate();
        this.requestStatus = postProjectResourceAllocationData.getRequestStatus();
        this.createdBy = postProjectResourceAllocationData.getCreatedBy();
        this.createdDate = postProjectResourceAllocationData.getCreatedDate();
        this.modifiedBy = postProjectResourceAllocationData.getModifiedBy();
        this.modifiedDate = postProjectResourceAllocationData.getModifiedDate();
        this.notify = postProjectResourceAllocationData.getNotify();
        this.viewedByPMIds = "";
        this.viewedByRMIds = "";
    }

}

