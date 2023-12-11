package com.forecastera.service.projectmanagement.commonResponseUtil;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Uttam Kachhad
 * @Create 19-05-2023
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Success<T> extends Response {

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private T data;

    @JsonProperty("duration")
    private Long duration;

}
