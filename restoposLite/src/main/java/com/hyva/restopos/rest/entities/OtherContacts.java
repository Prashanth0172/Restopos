package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class OtherContacts implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long otherContactId;
    private String fullName;
    private String prefixName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String contactNumber;
    private Date dob;
    @Column(columnDefinition="text")
    private String address;
    private String email;
    private String phoneNumber1;
    private String phoneNumber2;
    private String faxTelex;
    private String contactPerson;
    private String generalNote;
    private String website;
    private String gstCode;
    private String panNO;
    private String bankName;
    private String accountNo;
    private String iFSCCode;
    private String branchName;
    private String personIncharge;
    private String imageFile;
    private String locationId;
    private String useraccount_id;
    private String hiConnectStatus;
    private String hiConnectCompnyRegNo;
    private String description;
    private String subject;
    private String status;
    private String country;
    private String state;


}
