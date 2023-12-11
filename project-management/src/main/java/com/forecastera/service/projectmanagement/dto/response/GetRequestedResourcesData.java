package com.forecastera.service.projectmanagement.dto.response;
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
    private Integer requestedId;

    @JsonProperty("resourceName")
    private String resourceName;

  //  @JsonProperty("roleId")
  //  private String roleId;

    @JsonProperty("role")
    private String role;

    @JsonProperty("employmentType")
    private String employmentType;
  //  @JsonProperty("projectId")
 //   private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;

 //   @JsonProperty("startDate")
 //   private Date start_date;

 //   @JsonProperty("endDate")
  //  private Date end_date;

    @JsonProperty("allocationStartDate")
    private Date allocationStartDate;

    @JsonProperty("allocationEndDate")
    private Date allocationEndDate;

    @JsonProperty("requestedFte")
    private BigDecimal requestedFte;

    @JsonProperty("requestStatus")
    private String requestStatus;


 //   @JsonProperty("allocations")
 //   private List<AllResourceActiveAllocation> allocations;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

    @JsonProperty("avgAllocatedFte")
    private BigDecimal avgAllocatedFte;

    @JsonProperty("avgRequestedFte")
    private BigDecimal avgRequestedFte;

    @JsonProperty("lastActionByProjectManager")
    private String lastActionByProjectManager;

    @JsonProperty("notify")
    private Boolean notify;

//    public GetRequestedResourcesData(GetRequestedResourcesData getRequestedResourcesData){
//        this.requestedId = getRequestedResourcesData.getRequestedId();
//        this.resourceName = getRequestedResourcesData.getResourceName();
//        this.role = getRequestedResourcesData.getRole();
//        this.projectName = getRequestedResourcesData.getProjectName();
//        this.allocationStartDate = getRequestedResourcesData.getAllocationStartDate();
//        this.allocationEndDate = getRequestedResourcesData.getAllocationEndDate();
//        if(getRequestedResourcesData.getRequestedFte()!=null) {
//            this.requestedFte = getRequestedResourcesData.getRequestedFte();
//        }
//        else{
//            this.requestedFte = BigDecimal.valueOf(0d);
//        }
//    }
}
