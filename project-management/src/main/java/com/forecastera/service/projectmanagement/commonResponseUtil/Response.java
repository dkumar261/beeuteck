package com.forecastera.service.projectmanagement.commonResponseUtil;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Uttam Kachhad
 * @Create 19-05-2023
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private static final String TIMESTAMP_FIELD = "timestamp";

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date requestAt;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("message")
    private String message;

}
