package com.hyva.restopos.rest.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Admin
 */
@Data
@Entity

public class FormSetUp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long formsetupId;
    private String typename;
    private String typeprefix;
    private boolean include_date;
    private String nextref;
    private String transactionType;

    @Column(columnDefinition="text")
    private String termsDesc;
    private Long smsId;
    private Long emailId;
    private String locPrefix;

}
