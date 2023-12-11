package com.forecastera.service.usersettingsmanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 13-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostLoginCredentials {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}
