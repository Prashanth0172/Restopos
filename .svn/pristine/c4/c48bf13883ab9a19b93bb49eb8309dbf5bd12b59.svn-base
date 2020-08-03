package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
public class PaymentVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long couponid;
    private String name;
    private String code;
    private String type;
    private String discount;
    private String mintotal;
    private String redemptions;
    private String customerredemptions;
    private String orderrestriction;
    private String validity;
    private String description;
    private String status;
    private String dateadded;
    private String fixeddate;
    private String fixedfromtime;
    private String fixedtotime;
    private String periodstartdate;
    private String periodenddate;
    private String recurringevery;
    private String recurringfromtime;
    private String recurringtotime;

}
