package com.forecastera.service.usersettingsmanagement.dto.request;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResourceMgmtSkills {

    @JsonProperty("skill_id")
    private Integer skillId;

    @JsonProperty("skill")
    private String skill;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;

    @JsonProperty("parent_skill_id")
    private Integer parentSkillId;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("row_order")
    private Integer rowOrder;

}
