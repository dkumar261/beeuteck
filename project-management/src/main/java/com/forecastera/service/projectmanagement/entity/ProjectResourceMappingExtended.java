package com.forecastera.service.projectmanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 07-08-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.dto.utilityClass.AllResourceActiveAllocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "project_resource_mapping_extended")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectResourceMappingExtended {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "map_id")
    private Integer mapId;

    @Column(name = "allocation_date")
    private Date allocationDate;

    @Column(name = "fte")
    private BigDecimal fte;

    @Column(name = "requested_fte")
    private BigDecimal requestedFte;

    public ProjectResourceMappingExtended(AllResourceActiveAllocation allResourceActiveAllocation){
        this.id = allResourceActiveAllocation.getId();
        this.mapId = allResourceActiveAllocation.getMapId();
        this.allocationDate = allResourceActiveAllocation.getAllocationDate();
        this.fte = allResourceActiveAllocation.getAllocatedFte();
        this.requestedFte = allResourceActiveAllocation.getRequestedFte();
    }
}
