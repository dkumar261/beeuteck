package com.forecastera.service.projectmanagement.dto.response;
/*
 * @Author Sowjanya Aare
 * @Create 23-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetRequestedResourcesDataMaster {

    @JsonProperty("notifyCount")
    private Integer notifyCount;

    @JsonProperty("requestedResourcesData")
    List<GetRequestedResourcesData> requestedResourcesData;
}
