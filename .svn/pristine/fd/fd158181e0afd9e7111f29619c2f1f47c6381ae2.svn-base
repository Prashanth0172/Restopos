/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.entities;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author admin
 */
@Entity
@Data
public class CompanyInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long companyId;
    private String companyName;
    private String cfgFileName;
    private String dbName;
    private String financialYear;
    private String createdDate;
    private String cmpStatus;
    private String locationId;
    private String useraccount_id;
    @Column(length = 300)
    private String saasToken;

    public CompanyInfo(){};

    public CompanyInfo(long companyId){this.companyId = companyId;}
}
