package edu.exam.udp.datasource.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity(name = "employee")
public class Employee {
    @Id
    @GeneratedValue
    @Column(name = "code")
    private Integer id;
    @Column(name = "name", nullable = false, length = 24)
    private String name;
    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;
    @Column(name = "login", nullable = false, unique = true, length = 16)
    private String login;
    @Column(name = "password", nullable = false, length = 16)
    private String password;

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeHistory> employeeHistories;

}