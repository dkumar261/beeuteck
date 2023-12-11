package com.forecastera.service.usersettingsmanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 08-06-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.request.PostGeneralSettingsLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "location_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralSettingsLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "location")
    private String location;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "parent_location_id")
    private Integer parentLocationId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "row_order")
    private Integer rowOrder;

    public GeneralSettingsLocation(PostGeneralSettingsLocation postGeneralSettingsLocation){
        this.locationId = postGeneralSettingsLocation.getLocationId();
        this.location = postGeneralSettingsLocation.getLocation();
        this.createdBy = postGeneralSettingsLocation.getCreatedBy();
        this.createdDate = postGeneralSettingsLocation.getCreatedDate();
        this.modifiedBy = postGeneralSettingsLocation.getModifiedBy();
        this.modifiedDate = postGeneralSettingsLocation.getModifiedDate();
        this.parentLocationId = postGeneralSettingsLocation.getParentLocationId();
        this.isActive = postGeneralSettingsLocation.getIsActive();
        this.rowOrder = postGeneralSettingsLocation.getRowOrder();
    }
}
