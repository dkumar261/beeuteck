package com.forecastera.service.usersettingsmanagement.dto.response;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtSkills;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourceMgmtSkills {

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

    public GetResourceMgmtSkills(ResourceMgmtSkills resourceMgmtSkills){
        this.skillId = resourceMgmtSkills.getSkillId();
        this.skill = resourceMgmtSkills.getSkill();
        this.createdBy = resourceMgmtSkills.getCreatedBy();
        this.createdDate = resourceMgmtSkills.getCreatedDate();
        this.modifiedBy = resourceMgmtSkills.getModifiedBy();
        this.modifiedDate = resourceMgmtSkills.getModifiedDate();
        this.parentSkillId = resourceMgmtSkills.getParentSkillId();
        this.isActive = resourceMgmtSkills.getIsActive();
        this.rowOrder = resourceMgmtSkills.getRowOrder();
    }
}
