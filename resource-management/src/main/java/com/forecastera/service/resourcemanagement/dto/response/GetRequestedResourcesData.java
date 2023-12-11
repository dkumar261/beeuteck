package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 08-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetRequestedResourcesData {

    @JsonProperty("id")
    private Integer request_id;

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("roleId")
    private String role_id;

    @JsonProperty("role")
    private String role;

    @JsonProperty("employmentType")
    private String employment_type;

    @JsonProperty("department")
    private String department;

    @JsonProperty("projectId")
    private Integer project_id;

    @JsonProperty("projectName")
    private String project_name;

 //   @JsonProperty("startDate")
 //   private Date start_date;

 //   @JsonProperty("endDate")
  //  private Date end_date;

    @JsonProperty("allocationStartDate")
    private Date allocation_start_date;

    @JsonProperty("allocationEndDate")
    private Date allocation_end_date;

//    @JsonProperty("requestedFte")
//    private BigDecimal requested_fte;

    @JsonProperty("requestStatus")
    private String request_status;


 //   @JsonProperty("allocations")
 //   private List<AllResourceActiveAllocation> allocations;

    @JsonProperty("createdBy")
    private String created_by;

    @JsonProperty("createdDate")
    private Date created_date;

    @JsonProperty("modifiedBy")
    private String modified_by;

    @JsonProperty("modifiedDate")
    private Date modified_date;

    @JsonProperty("avgAllocatedFte")
    private BigDecimal avg_allocated_fte;

    @JsonProperty("avgRequestedFte")
    private BigDecimal avg_requested_fte;

    @JsonProperty("lastActionByProjectManager")
    private String last_action_by_project_manager;

    @JsonProperty("lastActionByResourceManager")
    private String last_action_by_resource_manager;

    @JsonProperty("viewedByIds")
    private String viewed_by_ids;

    @JsonProperty("notify")
    private Boolean notify;

    @JsonProperty("showBold")
    private Boolean show_bold;

    public GetRequestedResourcesData(GetRequestedResourcesDataForString getRequestedResourcesData){
        this.request_id = getRequestedResourcesData.getRequest_id();
        this.resource_id = getRequestedResourcesData.getResource_id();
        this.resource_name = getRequestedResourcesData.getResource_name();
        this.role_id = getRequestedResourcesData.getRole_id();
        this.role = getRequestedResourcesData.getRole();
        this.employment_type = getRequestedResourcesData.getEmployment_type();
        this.department = getRequestedResourcesData.getDepartment();
        this.project_id = getRequestedResourcesData.getProject_id();
        this.project_name = getRequestedResourcesData.getProject_name();
        this.allocation_start_date = getRequestedResourcesData.getAllocation_start_date();
        this.allocation_end_date = getRequestedResourcesData.getAllocation_end_date();
        this.request_status =getRequestedResourcesData.getRequest_status();
        this.created_by =getRequestedResourcesData.getCreated_by();
        this.created_date =getRequestedResourcesData.getCreated_date();
        this.modified_by =getRequestedResourcesData.getModified_by();
        this.modified_date =getRequestedResourcesData.getModified_date();
        this.avg_allocated_fte =getRequestedResourcesData.getAvg_allocated_fte();
        this.avg_requested_fte =getRequestedResourcesData.getAvg_requested_fte();
        this.last_action_by_project_manager =getRequestedResourcesData.getLast_action_by_project_manager();
        this.last_action_by_resource_manager =getRequestedResourcesData.getLast_action_by_resource_manager();
        this.viewed_by_ids =getRequestedResourcesData.getViewed_by_ids();
        this.notify = getRequestedResourcesData.getNotify().equals("1")?true:false;
        this.show_bold = getRequestedResourcesData.getShow_bold().equals("1")?true:false;
//        if(getRequestedResourcesData.getRequestedFte()!=null) {
//            this.requestedFte = getRequestedResourcesData.getRequestedFte();
//        }
//        else{
//            this.requestedFte = BigDecimal.valueOf(0d);
//        }
    }
}
