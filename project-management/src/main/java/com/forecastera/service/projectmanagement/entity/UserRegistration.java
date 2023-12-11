package com.forecastera.service.projectmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * @Author Sowjanya Aare
 * @Create 07-12-2023
 * @Description
 */
@Entity
@Table(name = "user_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role")
    private String role;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "emp_id")
    private String empId;

    @Column(name = "password")
    private String password;

public UserRegistration(UserRegistration userRegistration){
    this.firstName = userRegistration.getFirstName();
    this.lastName = userRegistration.getLastName();
    this.role = userRegistration.getRole();
    this.emailId = userRegistration.getEmailId();
    this.empId = userRegistration.getEmpId();
    this.password = userRegistration.getPassword();
    }
}