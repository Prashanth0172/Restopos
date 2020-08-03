package com.hyva.restopos.rest.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
public class AccountSetup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String defaultPrinter;
    private String unitPrice;
    private String discount;
    private String receiptFooter;
    private String mobile;
    private String retail;
    private String restaurentIndustry;
    private String restaurent;
    @Column(length = 50)
    private String printType;
    @Column(columnDefinition="text")
    private String reportPrinter;
    @Column(columnDefinition="text")
    private String kotPrinter;
    private String a4Printer;
    private String mobileBillPrinter;
    private String discountType;
    private boolean copytokot;
    private boolean printPreview;
    private String restaurant;
    private String billModel;
    @Column(columnDefinition="text")
    private String printerModel;
    private String printerModelMobile;
    private String pompOrderMaxTime;
    private String tokenOrder;
    private boolean promptKOT;
    private boolean notification;
    @Column(columnDefinition="text")
    private String printDetails;
    private String taxId;
    private String taxcheckbox;
    private String cashAcct;
    private String bankAcct;

}
