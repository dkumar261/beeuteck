package com.forecastera.service.usersettingsmanagement.dto.response;
/*
 * @Author Sowjanya Aare
 * @Create 21-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetRequestedResourcesDataMaster {

    @JsonProperty("notifyCount")
    private Integer notifyCount;

    @JsonProperty("getRequestedResourcesData")
    List<GetRequestedResourcesData> getRequestedResourcesData;
}
