package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long employeeId;
    private String employeeName;
    private String staff_group_id;
    private String status;
    private String username;
    private String password;
    private String staff_email;
    private String incentives;
    private String permission;
}
