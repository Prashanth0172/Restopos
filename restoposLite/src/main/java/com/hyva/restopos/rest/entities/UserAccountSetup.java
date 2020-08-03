package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
public class UserAccountSetup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int useraccountId;
    private String phone;
    private String userloginId;
    private String passwordUser;
    private String fullName;
    private boolean employeeflag;
    private boolean kitchenflag;
    private Long kitchenId;
    private String allLocations;
    private String email;
    @Column(length = 2000)
    private String securityQuestion;
    private String securityAnswer;
    private String status;
    private String reTypePassword;
    @OneToOne
    private UserAccessRights userAccessRights;

}
