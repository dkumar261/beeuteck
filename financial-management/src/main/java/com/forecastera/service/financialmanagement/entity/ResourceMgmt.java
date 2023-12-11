package com.forecastera.service.financialmanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

//@NamedNativeQuery(name = "ResourceMgmt.getResourcesByRoles",
//        query = "select cast(rm.roleId as INTEGER) as roleId, count(rm) as resourceCount, r.role as roleName " +
//                "from (select unnest(string_to_array(p.role,',')) as roleId, date_of_join from resource_master as p) as rm " +
//                "inner join role_master as r on cast(rm.roleId as INTEGER) = r.role_id " +
//                "where rm.date_of_join>= :startDate and rm.date_of_join <= :endDate " +
//                "group by rm.roleId, r.role",
//        resultSetMapping = "Mapping.GetResourcesByRoles")
//@SqlResultSetMapping(name = "Mapping.GetResourcesByRoles",
//        classes = @ConstructorResult(targetClass = GetResourcesByRoles.class, columns = {
//                @ColumnResult(name = "roleId"),
//                @ColumnResult(name = "resourceCount"),
//                @ColumnResult(name = "roleName")
//        }))

@Entity
@Table(name = "resource_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Integer resourceId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "date_of_join")
    private Date dateOfJoin;

    @Column(name = "location")
    private String location;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "role")
    private String role;

    @Column(name = "reporting_mgr")
    private Integer reportingMgr;

    @Column(name = "cost_per_hour")
    private Integer costPerHour;

    @Column(name = "fte_rate")
    private BigInteger fteRate;

    @Column(name = "variable")
    private BigInteger variable;

    @Column(name = "bonus")
    private BigInteger bonus;

    @Column(name = "skills")
    private String skills;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "emp_type_id")
    private Integer empTypeId;

    @Column(name = "emp_location")
    private String empLocation;

    @Column(name = "last_working_date")
    private Date lastWorkingDate;
}
