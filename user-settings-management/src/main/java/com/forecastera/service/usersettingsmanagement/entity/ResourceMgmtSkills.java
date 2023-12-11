package com.forecastera.service.usersettingsmanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.request.PostResourceMgmtSkills;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "skill_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmtSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Integer skillId;

    @Column(name = "skill")
    private String skill;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "parent_skill_id")
    private Integer parentSkillId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "row_order")
    private Integer rowOrder;

    public ResourceMgmtSkills(PostResourceMgmtSkills postResourceMgmtSkills){
        this.skillId = postResourceMgmtSkills.getSkillId();
        this.skill = postResourceMgmtSkills.getSkill();
        this.createdBy = postResourceMgmtSkills.getCreatedBy();
        this.createdDate = postResourceMgmtSkills.getCreatedDate();
        this.modifiedBy = postResourceMgmtSkills.getModifiedBy();
        this.modifiedDate = postResourceMgmtSkills.getModifiedDate();
        this.parentSkillId = postResourceMgmtSkills.getParentSkillId();
        this.isActive = postResourceMgmtSkills.getIsActive();
        this.rowOrder = postResourceMgmtSkills.getRowOrder();
    }
}
