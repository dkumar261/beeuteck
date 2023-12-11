package com.forecastera.service.resourcemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/*
 * @Author Uttam Kachhad
 * @Create 19-05-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourcesByRoles {

    @JsonProperty("roleId")
    private Integer role_id;

    @JsonProperty("resourceCount")
    private Integer resource_count;

    @JsonProperty("roleName")
    private String role_name;
}
