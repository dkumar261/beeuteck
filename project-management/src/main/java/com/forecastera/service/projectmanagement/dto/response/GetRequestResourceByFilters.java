package com.forecastera.service.projectmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 17-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.dto.utilityClass.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetRequestResourceByFilters {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("role")
    private String role;

    @JsonProperty("skills")
    private String skills;

//    @JsonProperty("location")
//    private String location;

    @JsonProperty("employmentType")
    private String employmentType;

    @JsonProperty("employeeLocation")
    private String employeeLocation;

    @JsonProperty("dailyAllocations")
    List<DailyAvailableFteData> dailyAllocations;

    @JsonProperty("weeklyFteSumData")
    List<ResourceWeeklyFteSumData> weeklyFteSumData;

    @JsonProperty("monthlyFteSumData")
    List<ResourceMonthlyFteSumData> monthlyFteSumData;

    public GetRequestResourceByFilters(AddResource addResource, List<DailyAvailableFteData> dailyAllocations,
                                       List<ResourceWeeklyFteSumData> weeklyFteSumData, List<ResourceMonthlyFteSumData> monthlyFteSumData){
        this.resourceId = addResource.getResource_id();
        this.resourceName = addResource.getResource_name();
        this.role = addResource.getRole();
        this.skills = addResource.getSkill();
//        this.location = addResource.getLocation();
        this.employmentType = addResource.getEmployee_type();
        this.employeeLocation = addResource.getEmployee_location();
        this.dailyAllocations = dailyAllocations;
        this.weeklyFteSumData = weeklyFteSumData;
        this.monthlyFteSumData = monthlyFteSumData;
    }
}
