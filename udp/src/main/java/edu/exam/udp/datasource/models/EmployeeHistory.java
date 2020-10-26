package edu.exam.udp.datasource.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "employee_history")
@Check(constraints = "dismiss >= hire and manager > 0")
public class EmployeeHistory {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "position", nullable = false, length = 24)
    private String position;
    @Column(name = "manager", nullable = false)
    private Integer managerId;
    @Column(name = "hire", nullable = false)
    private LocalDate hire;
    @Column(name = "dismiss")
    private LocalDate dismiss;
    @ManyToOne
    @JoinColumn(name = "code", nullable = false)
    private Employee employee;
}