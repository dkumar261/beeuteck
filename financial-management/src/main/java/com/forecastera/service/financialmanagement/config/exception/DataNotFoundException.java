package com.forecastera.service.financialmanagement.config.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @Author Kanishk Vats
 * @Create 17-07-2023
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataNotFoundException extends RuntimeException {

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("message")
    private String message;

}
