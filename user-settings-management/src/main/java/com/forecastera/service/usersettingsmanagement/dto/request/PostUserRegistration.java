package com.forecastera.service.usersettingsmanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 08-12-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUserRegistration {

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("role")
    private String role;

    @JsonProperty("emailId")
    private String emailId;

    @JsonProperty("empId")
    private String empId;

    @JsonProperty("password")
    private String password;
}
